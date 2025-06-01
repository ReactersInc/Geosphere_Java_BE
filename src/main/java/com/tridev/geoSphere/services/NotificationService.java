package com.tridev.geoSphere.services;

import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender mailSender;
    private final UserRepo userRepository;

    // In a real application, you would use Firebase Cloud Messaging,
    // WebSockets, or another push notification service

    public void sendGeofenceExitNotification(Long creatorId, Long userId, Long geofenceId, String geofenceName) {
        // Get user details
        UserEntity creator = userRepository.findByIdAndStatus(creatorId, Status.ACTIVE.getValue())
                .orElseThrow(() -> new RuntimeException("Creator not found"));
        UserEntity user = userRepository.findByIdAndStatus(userId, Status.ACTIVE.getValue())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Send email notification
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(creator.getEmail());
        message.setSubject("Geofence Alert: User has exited " + geofenceName);
        message.setText(String.format(
                "User %s (%s) has exited the geofence %s.",
                user.getFirstName(), user.getEmail(), geofenceName
        ));

        mailSender.send(message);

        // In a real app, you would also send push notifications
        // sendPushNotification(creatorId, "User " + user.getName() + " has exited geofence " + geofenceName);

        log.info("Exit notification sent to creator {} about user {} exiting geofence {}",
                creatorId, userId, geofenceId);
    }

    public void sendGeofenceEntryNotification(Long creatorId, Long userId, Long geofenceId, String geofenceName) {
        // Similar implementation as above for entry notifications
        UserEntity creator = userRepository.findByIdAndStatus(creatorId, Status.ACTIVE.getValue())
                .orElseThrow(() -> new RuntimeException("Creator not found"));
        UserEntity user = userRepository.findByIdAndStatus(userId, Status.ACTIVE.getValue())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(creator.getEmail());
        message.setSubject("Geofence Alert: User has entered " + geofenceName);
        message.setText(String.format(
                "User %s (%s) has entered the geofence %s.",
                user.getFirstName(), user.getEmail(), geofenceName
        ));

        mailSender.send(message);

        log.info("Entry notification sent to creator {} about user {} entering geofence {}",
                creatorId, userId, geofenceId);
    }

    // In real application, implement push notification service
    private void sendPushNotification(Long userId, String message) {
        // Implementation using Firebase Cloud Messaging or similar
        log.info("Push notification to user {}: {}", userId, message);
    }
}
