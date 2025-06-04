package com.tridev.geoSphere.services;

import com.google.firebase.messaging.*;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.FCMToken.NotificationDTO;
import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.dto.Notifications.NotificationResponseDTO;
import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import com.tridev.geoSphere.entities.sql.NotificationEntity;
import com.tridev.geoSphere.enums.NotificationStatus;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.mappers.FCMTokenMapper;
import com.tridev.geoSphere.repositories.sql.FCMTokenRepository;
import com.tridev.geoSphere.repositories.sql.NotificationRepository;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FcmTokenService {

    @Autowired
    private FCMTokenRepository fcmTokenRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private FCMTokenMapper fcmTokenMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public BaseResponse storeToken(StoreTokenDTO storeTokenDTO) {

        Long userId = jwtUtil.getUserIdFromToken();
        // Chmeck if a record exists with the sae userId and deviceId
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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public BaseResponse sendNotification(String token, String title, String body, String notificationType) throws BadRequestException {
        if (token == null || token.trim().isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.INVALID_FCM_TOKEN);
        }

        Long userId = jwtUtil.getUserIdFromToken();
        List<FCMTokenEntity> fcmTokens = fcmTokenRepository.findAllByToken(token);

        if (fcmTokens == null || fcmTokens.isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        // Group by unique deviceId
        Map<String, List<FCMTokenEntity>> deviceGroupedTokens = fcmTokens.stream()
                .collect(Collectors.groupingBy(FCMTokenEntity::getDeviceId));

        List<NotificationEntity> notificationsToSave = new ArrayList<>();

        for (Map.Entry<String, List<FCMTokenEntity>> entry : deviceGroupedTokens.entrySet()) {
            String deviceId = entry.getKey();
            List<FCMTokenEntity> tokenGroup = entry.getValue();
            FCMTokenEntity representativeToken = tokenGroup.get(0); // Pick one as representative

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build()
                    )
                    .putData("deviceId", deviceId)
                    .build();

            boolean sent = false;
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("Successfully sent message for deviceId {}: {}", deviceId, response);
                sent = true;
            } catch (FirebaseMessagingException fme) {
                log.error("FirebaseMessagingException for deviceId {}: {}", deviceId, fme.getMessage());
            } catch (Exception e) {
                log.error("Exception while sending message for deviceId {}", deviceId, e);
            }

            String status = sent ? NotificationStatus.SUCCESS.getValue() : NotificationStatus.FAILED.getValue();

            // Save a notification record for each token mapped to this deviceId
            for (FCMTokenEntity fcmTokenEntity : tokenGroup) {
                NotificationEntity notification = new NotificationEntity();
                notification.setUserId(fcmTokenEntity.getUserId().intValue());
                notification.setNotificationType(notificationType);
                notification.setMessage(body);
                notification.setFcmTokenId(fcmTokenEntity.getId());
                notification.setTitle(title);
                notification.setStatus(status);
                notification.setSentAt(LocalDateTime.now());
                notification.setCreatedAt(LocalDateTime.now());
                notification.setSentBy(userId.intValue());
                notificationsToSave.add(notification);
            }
        }

        notificationRepository.saveAll(notificationsToSave);

        return GeosphereServiceUtility.getBaseResponse("Notification processing complete. Check status for each device.");
    }


    public BaseResponse fetchUserNotification(Integer pageNo, Integer pageSize, String sortOrder) throws BadRequestException {
        Long userId = jwtUtil.getUserIdFromToken();
        if (userId == null) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        // Determine sorting direction
        Sort.Direction direction;
        if ("ASC".equalsIgnoreCase(sortOrder)) {
            direction = Sort.Direction.ASC;
        } else if ("DESC".equalsIgnoreCase(sortOrder)) {
            direction = Sort.Direction.DESC;
        } else {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        // Create a Pageable sorted by createdAt and then by id
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, "createdAt").and(Sort.by(direction, "id")));

        // Fetch paged notifications
        Page<NotificationEntity> notifications = notificationRepository.findByUserId(userId.intValue(), pageable);
        NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO();
        notificationResponseDTO.setNotification(notifications.getContent());
        notificationResponseDTO.setPageNo(pageNo);
        notificationResponseDTO.setPageSize(pageSize);
        notificationResponseDTO.setTotalElements(notifications.getTotalElements());
        notificationResponseDTO.setTotalPages(notifications.getTotalPages());
        notificationResponseDTO.setSortOrder(sortOrder);
        log.info("Fetched notifications: {}", notificationResponseDTO);
        return GeosphereServiceUtility.getBaseResponse(notificationResponseDTO);
    }
}
