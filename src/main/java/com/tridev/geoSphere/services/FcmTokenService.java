package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.entities.FCMTokenEntity;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.mappers.FCMTokenMapper;
import com.tridev.geoSphere.repositories.FCMTokenRepository;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class FcmTokenService {

    @Autowired
    private FCMTokenRepository fcmTokenRepository;


    @Autowired
    private FCMTokenMapper fcmTokenMapper;

    @Autowired
    private JwtUtil jwtUtil;





    public BaseResponse storeToken(StoreTokenDTO storeTokenDTO) {

        Long userId = jwtUtil.getUserIdFromToken();
        // Check if a record exists with the same userId and deviceId
        Optional<FCMTokenEntity> existingEntity = fcmTokenRepository.findByUserIdAndDeviceId(
                userId,
                storeTokenDTO.getDeviceId()
        );

        if (existingEntity.isPresent()) {

            log.info("user is {}", userId);
            // Update the existing token
            FCMTokenEntity entity = existingEntity.get();
            entity.setToken(storeTokenDTO.getToken());
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(userId);
            fcmTokenRepository.save(entity);
            log.info("fcm entity: {}", entity);
        } else {
            // Check if there are any records with the same userId but different deviceId
            boolean userExists = fcmTokenRepository.existsByUserId(userId);

            if (userExists) {

                log.info("user is {}", userId);
                // Same user, new device - create new entry
                FCMTokenEntity newEntity = fcmTokenMapper.toEntity(storeTokenDTO);
                newEntity.setUserId(userId);
                newEntity.setCreatedBy(userId);
                newEntity.setUpdatedAt(LocalDateTime.now());
                fcmTokenRepository.save(newEntity);
            } else {
                log.info("user is {}", userId);
                // New user - create new entry
                FCMTokenEntity newEntity = fcmTokenMapper.toEntity(storeTokenDTO);
                newEntity.setUserId(userId);
                newEntity.setCreatedBy(userId);

                fcmTokenRepository.save(newEntity);
            }
        }

        return GeosphereServiceUtility.getBaseResponseWithoutData();
    }
}
