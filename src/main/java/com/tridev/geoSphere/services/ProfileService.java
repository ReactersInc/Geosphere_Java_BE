package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.Profile.ProfileDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.mappers.UserMapper;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {



    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper registerUserMapper;


    @Autowired
    private JwtUtil jwtUtil;


    public BaseResponse findUserByUserId() throws BadRequestException {

        Long userId = jwtUtil.getUserIdFromToken();

        Optional<UserEntity> entity = userRepo.findById(userId);

        if(entity.isPresent()){

            ProfileDTO  user =  registerUserMapper.toProfile(entity.get());

            return GeosphereServiceUtility.getBaseResponse(user);
        }


        throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);



    }







}
