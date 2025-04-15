package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.RegisterUserDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.mappers.RegisterUserMapper;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GenerateOTPUtil;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepo registerUserRepo;

    @Autowired
    private RegisterUserMapper registerUserMapper;

    @Autowired
    private GenerateOTPUtil generateOTPUtil;

    @Autowired
    private EmailService emailService;


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



}
