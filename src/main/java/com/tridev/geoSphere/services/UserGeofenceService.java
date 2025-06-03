package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.GeofenceRequest.GeofenceRequestDTO;
import com.tridev.geoSphere.dto.GeofenceRequest.GeofenceRequestResponseDTO;
import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceRequestDTO;
import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceResponseDTO;
import com.tridev.geoSphere.dto.common.PaginatedResponse;
import com.tridev.geoSphere.entities.mongo.UserLocation;
import com.tridev.geoSphere.entities.sql.*;
import com.tridev.geoSphere.enums.*;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.exceptions.ResourceNotFoundException;
import com.tridev.geoSphere.mappers.GeofenceRequestMapper;
import com.tridev.geoSphere.mappers.GeofenceRequestResponseMapper;
import com.tridev.geoSphere.mappers.UserGeofenceMapper;
import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
import com.tridev.geoSphere.repositories.sql.*;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserGeofenceService {

    @Autowired
    private UserGeofenceRepository userGeofenceRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserGeofenceMapper userGeofenceMapper;

    @Autowired
    private GeofenceRepository geofenceRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GeofenceRequestMapper geofenceRequestMapper;

    @Autowired
    private GeofenceRequestRepository geofenceRequestRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private GeofenceRequestResponseMapper geofenceRequestResponseMapper;

    @Autowired
    private UserContactsRepository userContactsRepository;

    @Autowired
    private FcmTokenService fcmTokenService;

    @Autowired FCMTokenRepository fcmTokenRepository;




    @Transactional
    public BaseResponse addPeopleInGeofence(UserGeofeceRequestDTO userGeofeceRequestDTO) throws Exception{

        Optional<UserEntity> users = userRepo.findByEmailAndStatus(userGeofeceRequestDTO.getEmail(), Status.ACTIVE.getValue());
        if(users.isPresent()){

            UserEntity user = users.get();
            Optional<UserGeofenceEntity> byUserIdAndGeofenceIdAndStatus = userGeofenceRepo.findByUserIdAndGeofenceIdAndStatus(user.getId(), userGeofeceRequestDTO.getGeofenceId(), Status.ACTIVE.getValue());

            if(byUserIdAndGeofenceIdAndStatus.isPresent()){
                throw new BadRequestException(CommonValidationConstant.PERSON_ALREADY_EXISTS);
            }
            Optional<FCMTokenEntity> byUserId = fcmTokenRepository.findByUserId(user.getId());
            if(byUserId.isPresent()){
                FCMTokenEntity fcmTokenEntity = byUserId.get();

                fcmTokenService.sendNotification(fcmTokenEntity.getToken(), "Geofence Invitation", "You have been invited to join the geofence: " + userGeofeceRequestDTO.getGeofenceId() + ". Please accept the invitation to join.", String.valueOf(NotificationType.INVITATION));

            }
        }

        // Check if the user is already in the geofence



        Long userId = jwtUtil.getUserIdFromToken();

        Optional<GeofenceRequestEntity> byUserIdAndRecipientEmailAndGeofenceIdAndStatus = geofenceRequestRepository.findByUserIdAndRecipientEmailAndGeofenceIdAndStatus(userId, userGeofeceRequestDTO.getEmail(), userGeofeceRequestDTO.getGeofenceId(), Status.PENDING.getValue());

        if(byUserIdAndRecipientEmailAndGeofenceIdAndStatus.isPresent()){
            throw new BadRequestException(CommonValidationConstant.REQUEST_ALREADY_SENT);
        }

        Optional<GeofenceEntity> geofence = geofenceRepository.findByIdAndStatus(userGeofeceRequestDTO.getGeofenceId(), Status.ACTIVE.getValue());

        if(geofence.isPresent()){
            GeofenceEntity geofenceEntity = geofence.get();

            boolean equals = geofenceEntity.getCreatedBy().equals(userId);

            if(!equals){
                throw new BadRequestException(CommonValidationConstant.GEOFENCE_NOT_FOUND);
            }





            GeofenceRequestDTO geofenceRequestDTO = new GeofenceRequestDTO();

            geofenceRequestDTO.setRecipientEmail(userGeofeceRequestDTO.getEmail());
            geofenceRequestDTO.setUserId(userId);
            GeofenceRequestEntity entity = geofenceRequestMapper.toEntity(geofenceRequestDTO);
            entity.setGeofenceId(userGeofeceRequestDTO.getGeofenceId());

            entity.setStatus(Status.PENDING.getValue());

            entity.setNotificationStatus(NotificationStatus.PENDING);









            Boolean email = emailService.sendEmail(userGeofeceRequestDTO.getEmail(),
                    "Invitation to join geofence",
                    "You have been invited to join the geofence: " + geofenceEntity.getName() + ". Please accept the invitation to join.");

            if(email){
                entity.setNotificationStatus(NotificationStatus.SUCCESS);
            }else {
                entity.setNotificationStatus(NotificationStatus.FAILED);
            }

            geofenceRequestRepository.save(entity);


        }

        return GeosphereServiceUtility.getBaseResponseWithoutData();


    }






    public BaseResponse getAllUsersInGeofence(Long geofenceId, int page, int size) throws Exception {
        try {
            // Validate the geofenceId
            if (geofenceId == null || geofenceId <= 0) {
                throw new BadRequestException(CommonValidationConstant.GEOFENCE_NOT_FOUND);
            }

            // Get all user-geofence relationships for this geofence
            List<UserGeofenceEntity> userGeofences = userGeofenceRepo.findByGeofenceId(geofenceId);

            // Create a map for quick lookup (userId -> UserGeofenceEntity)
            Map<Long, UserGeofenceEntity> userGeofenceMap = userGeofences.stream()
                    .collect(Collectors.toMap(UserGeofenceEntity::getUserId, Function.identity()));

            // Create pagination request
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            // Get paginated results of users in this geofence
            Page<UserEntity> usersPage = userGeofenceRepo.findUsersByGeofenceId(geofenceId, pageable);

            if (usersPage.isEmpty()) {
                throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
            }

            // Extract user IDs for batch location fetching
            List<Long> userIds = usersPage.getContent().stream()
                    .map(UserEntity::getId)
                    .collect(Collectors.toList());

            // Get latest locations in a single batch query
            List<UserLocation> latestLocations = userLocationRepository.findLatestLocationsForUsers(userIds);

            // Create a map for quick lookup (userId -> latest location)
            Map<Long, UserLocation> locationMap = latestLocations.stream()
                    .collect(Collectors.toMap(UserLocation::getUserId, Function.identity()));

            // Map to response DTOs with location and geofence status information
            List<UserGeofeceResponseDTO> responseList = usersPage.getContent().stream()
                    .map(user -> {
                        UserGeofeceResponseDTO dto = userGeofenceMapper.toResponseDTO(user);
                        UserLocation location = locationMap.get(user.getId());
                        UserGeofenceEntity userGeofence = userGeofenceMap.get(user.getId());

                        // Set isCurrentlyInside from UserGeofenceEntity if exists
                        if (userGeofence != null && location != null) {
                            dto.setCurrentlyInside(userGeofence.getIsCurrentlyInside());
                        } else {
                            // If no UserGeofenceEntity exists (shouldn't happen), default to false
                            dto.setCurrentlyInside(false);
                        }

                        if (location != null) {
                            // Location exists - populate all fields
                            dto.setLocationExists(true);
                            dto.setCurrentLocation(location.getLocation());
                            dto.setLastLocationUpdateTime(location.getTimestamp());
                            dto.setSpeed(location.getSpeed());
                            dto.setHeading(location.getHeading());
                        } else {
                            // Location doesn't exist - set flag and null fields
                            dto.setLocationExists(false);
                            dto.setCurrentLocation(null);
                            dto.setLastLocationUpdateTime(null);
                            dto.setSpeed(null);
                            dto.setHeading(null);
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            // Create paginated response
            PaginatedResponse<UserGeofeceResponseDTO> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setList(responseList);
            paginatedResponse.setPage(usersPage.getNumber());
            paginatedResponse.setSize(usersPage.getSize());
            paginatedResponse.setTotalElements(usersPage.getTotalElements());
            paginatedResponse.setTotalPages(usersPage.getTotalPages());

            return GeosphereServiceUtility.getBaseResponse(paginatedResponse);

        } catch (ResourceNotFoundException ex) {
            log.warn("No users found in geofence {}: {}", geofenceId, ex.getMessage());
            throw ex;
        } catch (BadRequestException ex) {
            log.warn("Bad request for geofence users: {}", ex.getMessage());
            throw ex;
        }
    }


    @Transactional(rollbackFor = Exception.class )
    public BaseResponse acceptGeofenceRequest(Long requestId) throws Exception {
        try {
            Long userId = jwtUtil.getUserIdFromToken();

            Optional<UserEntity> byIdAndStatus = userRepo.findByIdAndStatus(userId, Status.ACTIVE.getValue());



            Optional<GeofenceRequestEntity> geofenceRequest = geofenceRequestRepository.findByIdAndStatus(requestId, Status.PENDING.getValue());

            if (geofenceRequest.isPresent() && byIdAndStatus.isPresent()) {

                UserEntity user = byIdAndStatus.get();
                GeofenceRequestEntity requestEntity = geofenceRequest.get();
                if(!user.getEmail().equals(requestEntity.getRecipientEmail())){
                    throw new BadRequestException(CommonValidationConstant.REQUEST_NOT_FOUND);
                }
                UserGeofenceEntity userGeofenceEntity = new UserGeofenceEntity();
                userGeofenceEntity.setUserId(userId);
                userGeofenceEntity.setGeofenceId(requestEntity.getGeofenceId());
                userGeofenceEntity.setStatus(Status.ACTIVE.getValue());

                userGeofenceRepo.save(userGeofenceEntity);

                // Update the request status to ACCEPTED


                requestEntity.setStatus(Status.ACCEPTED.getValue());
                geofenceRequestRepository.save(requestEntity);

                return GeosphereServiceUtility.getBaseResponseWithoutData();
            } else {
                throw new BadRequestException(CommonValidationConstant.REQUEST_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error accepting geofence request: {}", e.getMessage());
            throw e;
        }

    }


    @Transactional
   public BaseResponse rejectGeofenceRequest(Long requestId) throws Exception {
        try {
            Long userId = jwtUtil.getUserIdFromToken();

            Optional<GeofenceRequestEntity> geofenceRequest = geofenceRequestRepository.findByIdAndStatus(requestId, Status.PENDING.getValue());

            if (geofenceRequest.isPresent()) {
                GeofenceRequestEntity requestEntity = geofenceRequest.get();


                // Update the request status to ACCEPTED


                requestEntity.setStatus(Status.REJECTED.getValue());
                geofenceRequestRepository.save(requestEntity);

                return GeosphereServiceUtility.getBaseResponseWithoutData();
            } else {
                throw new BadRequestException(CommonValidationConstant.REQUEST_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error accepting geofence request: {}", e.getMessage());
            throw e;
        }

    }



    public BaseResponse getGeofenceRequest(int page, int size) throws Exception {
        try {
            Long userId = jwtUtil.getUserIdFromToken();
            Optional<UserEntity> user = userRepo.findByIdAndStatus(userId, Status.ACTIVE.getValue());

            if(user.isPresent()) {
                UserEntity userEntity = user.get();
                String email = userEntity.getEmail();

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<GeofenceRequestEntity> geofenceRequests = geofenceRequestRepository.findByRecipientEmailAndStatus(
                        email, Status.PENDING.getValue(), pageable);

                if (geofenceRequests.isEmpty()) {
                    throw new BadRequestException(CommonValidationConstant.REQUEST_NOT_FOUND);
                }

                // Fetch all geofence IDs from the requests
                List<Long> geofenceIds = geofenceRequests.getContent().stream()
                        .map(GeofenceRequestEntity::getGeofenceId)
                        .collect(Collectors.toList());

                // Fetch all creator user IDs from the requests
                List<Long> creatorIds = geofenceRequests.getContent().stream()
                        .map(GeofenceRequestEntity::getUserId)
                        .collect(Collectors.toList());

                // Fetch all geofences in one query
                Map<Long, GeofenceEntity> geofenceMap = geofenceRepository.findAllById(geofenceIds).stream()
                        .collect(Collectors.toMap(GeofenceEntity::getId, Function.identity()));

                // Fetch all users in one query
                Map<Long, UserEntity> userMap = userRepo.findAllById(creatorIds).stream()
                        .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

                // Map to response DTO
                List<GeofenceRequestResponseDTO> responseList = geofenceRequests.getContent().stream()
                        .map(request -> {
                            GeofenceEntity geofence = geofenceMap.get(request.getGeofenceId());
                            UserEntity creator = userMap.get(request.getUserId());
                            return geofenceRequestResponseMapper.toResponseDTO(request, geofence, creator);
                        })
                        .collect(Collectors.toList());

                PaginatedResponse<GeofenceRequestResponseDTO> paginatedResponse = new PaginatedResponse<>();
                paginatedResponse.setList(responseList);
                paginatedResponse.setPage(geofenceRequests.getNumber());
                paginatedResponse.setSize(geofenceRequests.getSize());
                paginatedResponse.setTotalElements(geofenceRequests.getTotalElements());
                paginatedResponse.setTotalPages(geofenceRequests.getTotalPages());

                return GeosphereServiceUtility.getBaseResponse(paginatedResponse);
            } else {
                throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            log.warn("No geofence requests found: {}", ex.getMessage());
            throw ex;
        } catch (BadRequestException ex) {
            log.warn("Bad request for geofence requests: {}", ex.getMessage());
            throw ex;
        }
    }










}
