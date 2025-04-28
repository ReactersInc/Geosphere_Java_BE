package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.User.UserDetailsDTO;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.mappers.UserMapper;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper registerUserMapper;


    public BaseResponse getAllUsers(){
        List<UserEntity> all = userRepo.findAll();
        List<UserDetailsDTO> userDetails = registerUserMapper.toEntities(all);
        return GeosphereServiceUtility.getBaseResponse(userDetails);
    }
}
