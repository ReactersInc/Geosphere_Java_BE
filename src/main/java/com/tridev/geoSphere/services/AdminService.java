package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.UserDetailsDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import com.tridev.geoSphere.mappers.RegisterUserMapper;
import com.tridev.geoSphere.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RegisterUserMapper registerUserMapper;


    public ResponseEntity<List<UserDetailsDTO>> getAllUsers(){

        List<RegisterUserEntity> all = userRepo.findAll();
        List<UserDetailsDTO> userDetails = registerUserMapper.toEntities(all);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);

    }
}
