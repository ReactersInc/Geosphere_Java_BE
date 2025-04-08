package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.RegisterUserDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import com.tridev.geoSphere.mappers.RegisterUserMapper;
import com.tridev.geoSphere.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepo registerUserRepo;

    @Autowired
    private RegisterUserMapper registerUserMapper;


private static  final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public ResponseEntity<RegisterUserDTO> registerUser(RegisterUserDTO registerUserDTO, String userType){
        RegisterUserEntity entity = registerUserMapper.toEntity(registerUserDTO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRegisteredAt(LocalDateTime.now());

        entity.setRole(String.valueOf(userType));
        RegisterUserEntity savedEntity = registerUserRepo.save(entity);
        return new ResponseEntity<>(registerUserDTO, HttpStatus.CREATED);
    }


//    public ResponseEntity<RegisterUserDTO> updateUser(){
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Object details = authentication.getDetails();
//
//
//    }



}
