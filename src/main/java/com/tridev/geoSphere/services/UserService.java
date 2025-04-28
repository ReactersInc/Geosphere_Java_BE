package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.User.PublicProfileResponseDTO;
import com.tridev.geoSphere.dto.authentications.RegisterUserDTO;
import com.tridev.geoSphere.dto.common.PaginatedResponse;
import com.tridev.geoSphere.entities.sql.ConnectionRequestEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.mappers.UserMapper;
import com.tridev.geoSphere.repositories.sql.ConnectionRequestRepository;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GenerateOTPUtil;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepo registerUserRepo;

    @Autowired
    private UserMapper registerUserMapper;

    @Autowired
    private GenerateOTPUtil generateOTPUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;




private static  final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Transactional
    public BaseResponse registerUser(RegisterUserDTO registerUserDTO, String userType) {
        UserEntity entity = registerUserMapper.toEntity(registerUserDTO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsVerified(false);
        entity.setRole(userType);

        // Generate and set OTP
        String otp = generateOTPUtil.OTP();
        entity.setOtp(otp);
        entity.setStatus(Status.PENDING.getValue());

        registerUserRepo.save(entity);

        // Send email with OTP
        emailService.sendEmail(
                entity.getEmail(),
                "Verify Email to Register on GeoFence App",
                "Your OTP to verify your Email is: " + otp
        );

        return GeosphereServiceUtility.getBaseResponseWithoutData();
    }








//    private String generateCustomUserId() {
//        Optional<RegisterUserEntity> lastUserOpt = registerUserRepo.findTopByOrderByUserIdDesc();
//
//        int nextId = 101;
//        if (lastUserOpt.isPresent()) {
//            String lastId = lastUserOpt.get().getUserId(); // e.g., "U-105"
//            int num = Integer.parseInt(lastId.split("-")[1]);
//            nextId = num + 1;
//        }
//
//        return "U-" + nextId;
//    }





//    public ResponseEntity<RegisterUserDTO> updateUser(){
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Object details = authentication.getDetails();
//
//
//    }


    public BaseResponse getPublicProfilesWithStatus(
            int page, int size, String sortBy, String direction) {

        Long currentUserId = jwtUtil.getUserIdFromToken();
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get all public users excluding current user
        Page<UserEntity> publicUsers = registerUserRepo.findByPublicProfileTrueAndIdNot(currentUserId, pageable);

        // Get all connection requests where current user is requester
        List<ConnectionRequestEntity> myRequests = connectionRequestRepository.findByRequesterUserId(currentUserId);

        // Map to DTO with connection status
        List<PublicProfileResponseDTO> content = publicUsers.stream()
                .filter(user -> shouldIncludeUser(user, myRequests))
                .map(user -> mapToPublicProfileDTO(user, myRequests))
                .toList();

        // Create paginated response
        PaginatedResponse<PublicProfileResponseDTO> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setList(content);
        paginatedResponse.setPage(publicUsers.getNumber());
        paginatedResponse.setSize(publicUsers.getSize());
        paginatedResponse.setTotalElements((long) content.size()); // Adjusted for filtered count
        paginatedResponse.setTotalPages((int) Math.ceil((double) content.size() / size));

        return GeosphereServiceUtility.getBaseResponse(paginatedResponse);
    }

    private boolean shouldIncludeUser(UserEntity user, List<ConnectionRequestEntity> myRequests) {
        // Check if there's any request with status 11 (ACCEPTED) for this user
        boolean hasAcceptedRequest = myRequests.stream()
                .anyMatch(cr -> cr.getTargetUserId().equals(user.getId()) && cr.getStatus() == 11);

        // Include user if there's no accepted request
        return !hasAcceptedRequest;
    }

    private PublicProfileResponseDTO mapToPublicProfileDTO(UserEntity user, List<ConnectionRequestEntity> myRequests) {
        PublicProfileResponseDTO dto = registerUserMapper.toPublicProfileDTO(user);

        // Check connection status
        Optional<ConnectionRequestEntity> connectionRequest = myRequests.stream()
                .filter(cr -> cr.getTargetUserId().equals(user.getId()))
                .findFirst();

        if (connectionRequest.isPresent()) {
            int status = connectionRequest.get().getStatus();
            dto.setRequested(status == 1); // PENDING
            dto.setConnected(status == 11); // ACCEPTED
        } else {
            dto.setRequested(false);
            dto.setConnected(false);
        }

        return dto;
    }

    }



