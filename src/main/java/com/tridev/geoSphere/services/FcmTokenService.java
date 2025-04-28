package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import com.tridev.geoSphere.mappers.FCMTokenMapper;
import com.tridev.geoSphere.repositories.sql.FCMTokenRepository;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
            entity.setIsLoggedIn(true);
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
                newEntity.setIsLoggedIn(true);
                newEntity.setUpdatedAt(LocalDateTime.now());
                fcmTokenRepository.save(newEntity);
            } else {
                log.info("user is {}", userId);
                // New user - create new entry
                FCMTokenEntity newEntity = fcmTokenMapper.toEntity(storeTokenDTO);
                newEntity.setUserId(userId);
                newEntity.setCreatedBy(userId);
                newEntity.setIsLoggedIn(true);

                fcmTokenRepository.save(newEntity);
            }
        }

        return GeosphereServiceUtility.getBaseResponseWithoutData();
    }
}
