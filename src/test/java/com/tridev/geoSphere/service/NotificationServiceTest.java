package com.tridev.geoSphere.service;

import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendGeofenceExitNotification_shouldSendEmail() {
        // Arrange
        Long creatorId = 1L;
        Long userId = 2L;
        Long geofenceId = 3L;
        String geofenceName = "Test Geofence";

        UserEntity creator = new UserEntity();
        creator.setId(creatorId);
        creator.setEmail("creator@example.com");
        creator.setFirstName("Creator");

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setFirstName("User");

        when(userRepository.findByIdAndStatus(creatorId,Status.ACTIVE.getValue())).thenReturn(Optional.of(creator));
        when(userRepository.findByIdAndStatus(userId, Status.ACTIVE.getValue())).thenReturn(Optional.of(user));

        // Act
        notificationService.sendGeofenceExitNotification(creatorId, userId, geofenceId, geofenceName);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendGeofenceEntryNotification_shouldSendEmail() {
        // Arrange
        Long creatorId = 1L;
        Long userId = 2L;
        Long geofenceId = 3L;
        String geofenceName = "Test Geofence";

        UserEntity creator = new UserEntity();
        creator.setId(creatorId);
        creator.setEmail("creator@example.com");
        creator.setFirstName("Creator");

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setFirstName("User");

        when(userRepository.findByIdAndStatus(creatorId,Status.ACTIVE.getValue())).thenReturn(Optional.of(creator));
        when(userRepository.findByIdAndStatus(userId,Status.ACTIVE.getValue())).thenReturn(Optional.of(user));

        // Act
        notificationService.sendGeofenceEntryNotification(creatorId, userId, geofenceId, geofenceName);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}