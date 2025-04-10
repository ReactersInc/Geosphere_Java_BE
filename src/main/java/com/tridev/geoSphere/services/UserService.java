package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.RegisterUserDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import com.tridev.geoSphere.mappers.RegisterUserMapper;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.utils.GenerateOTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public ResponseEntity<RegisterUserDTO> registerUser(RegisterUserDTO registerUserDTO, String userType){
        RegisterUserEntity entity = registerUserMapper.toEntity(registerUserDTO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRegisteredAt(LocalDateTime.now());
        String newUserId= generateCustomUserId();

        log.info("the new userId is {}", newUserId);
        entity.setUserId(newUserId);
        //generate and set OTP
        String otp= generateOTPUtil.OTP();
        entity.setOtp(otp);

//        entity.setRole(String.valueOf(userType));
        RegisterUserEntity savedEntity = registerUserRepo.save(entity);

        // send mail
        emailService.sendEmail(entity.getEmail(), "Verify Email to Register on GeoFence App", "your one time OTP to verify your Email and continue to GeoFencing Application is {}"+ entity.getOtp());

        return new ResponseEntity<>(registerUserDTO, HttpStatus.CREATED);
    }


    private String generateCustomUserId() {
        Optional<RegisterUserEntity> lastUserOpt = registerUserRepo.findTopByOrderByUserIdDesc();

        int nextId = 101;
        if (lastUserOpt.isPresent()) {
            String lastId = lastUserOpt.get().getUserId(); // e.g., "U-105"
            int num = Integer.parseInt(lastId.split("-")[1]);
            nextId = num + 1;
        }

        return "U-" + nextId;
    }


//    public ResponseEntity<RegisterUserDTO> updateUser(){
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Object details = authentication.getDetails();
//
//
//    }



}
