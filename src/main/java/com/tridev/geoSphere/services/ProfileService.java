package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.Profile.ProfileDTO;
import com.tridev.geoSphere.dto.User.GeofenceDetailsDTO;
import com.tridev.geoSphere.dto.User.UserGeofenceDTO;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
import com.tridev.geoSphere.enums.ResponseStatus;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.mappers.UserMapper;
import com.tridev.geoSphere.repositories.sql.*;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserGeofenceRepository userGeofenceRepo;

    @Autowired
    private GeofenceRepository geofenceRepo;

    @Autowired
    private UserContactsRepository userContactsRepo;

    @Autowired
    private GeofenceRequestRepository geofenceRequestRepo;

    @Autowired
    private ConnectionRequestRepository connectionRequestRepo;

    @Autowired
    private UserMapper registerUserMapper;


    @Autowired
    private JwtUtil jwtUtil;


    @Transactional
    public BaseResponse findUserByUserId() throws BadRequestException {
        Long userId = jwtUtil.getUserIdFromToken();
        Optional<UserEntity> entity = userRepo.findById(userId);

        if (entity.isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
        }

        UserEntity user = entity.get();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmail(user.getEmail());
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());

        // Prepare UserGeofenceDTO
        UserGeofenceDTO userGeofenceDTO = new UserGeofenceDTO();

        // Total Geofences
        Integer totalGeofence = userGeofenceRepo.countByUserId(userId);
        Integer totalGeofenceInUse = userGeofenceRepo.countByUserIdAndStatus(userId, Status.ACTIVE.getValue());
        Integer totalGeofenceNotInUse = totalGeofence - totalGeofenceInUse;

        // Total users in geofence = distinct userId count in UserGeofenceEntity
        List<Long> geofenceIds = userGeofenceRepo.findAllGeofenceIdsByUserId(userId);
        Integer totalUsersInGeofence = geofenceIds.isEmpty() ? 0 :
                userGeofenceRepo.countDistinctUserIdByGeofenceIds(geofenceIds);


        // Total connections
        Integer totalConnections = userContactsRepo.countByUserIdAndStatus(userId, Status.ACTIVE.getValue());

        // Pending geofence requests
        Integer pendingGeofenceRequests = geofenceRequestRepo.countByUserIdAndResponseStatus(userId, ResponseStatus.PENDING);

        // Pending connection requests
        Integer pendingConnectionRequests = connectionRequestRepo.countByTargetUserIdAndStatus(userId, Status.PENDING.getValue());

        // Currently inside geofence
        Optional<UserGeofenceEntity> currentGeofenceOpt = userGeofenceRepo
                .findFirstByUserIdAndIsCurrentlyInsideTrue(userId);

        GeofenceDetailsDTO inWitchGeofence = null;
        if (currentGeofenceOpt.isPresent()) {
            Long geofenceId = currentGeofenceOpt.get().getGeofenceId();
            Optional<GeofenceEntity> geofenceEntity = geofenceRepo.findById(geofenceId);
            if (geofenceEntity.isPresent()) {
                GeofenceEntity gf = geofenceEntity.get();
                inWitchGeofence = new GeofenceDetailsDTO(gf.getId(), gf.getName(), gf.getDescription());
            }
        }

        userGeofenceDTO.setTotalGeofence(totalGeofence);
        userGeofenceDTO.setTotalGeofenceInUse(totalGeofenceInUse);
        userGeofenceDTO.setTotalGeofenceNotInUse(totalGeofenceNotInUse);
        userGeofenceDTO.setTotalUsersInGeofence(totalUsersInGeofence);
        userGeofenceDTO.setTotalConnections(totalConnections);
        userGeofenceDTO.setPendingGeofenceRequests(pendingGeofenceRequests);
        userGeofenceDTO.setPendingConnectionRequests(pendingConnectionRequests);
        userGeofenceDTO.setInWitchGeofence(inWitchGeofence);

        profileDTO.setUserGeofenceDetails(userGeofenceDTO);

        return GeosphereServiceUtility.getBaseResponse(profileDTO);
    }
}
