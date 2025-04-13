package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.entities.FCMTokenEntity;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.mappers.FCMTokenMapper;
import com.tridev.geoSphere.repositories.FCMTokenRepository;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FcmTokenService {

    @Autowired
    private FCMTokenRepository fcmTokenRepository;


    @Autowired
    private FCMTokenMapper fcmTokenMapper;





    public BaseResponse storeToken(StoreTokenDTO storeTokenDTO){



        FCMTokenEntity entity = fcmTokenMapper.toEntity(storeTokenDTO);



        fcmTokenRepository.save(entity);

        return GeosphereServiceUtility.getBaseResponseWithoutData();



    }
}
