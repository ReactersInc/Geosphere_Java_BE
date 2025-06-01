package com.tridev.geoSphere.dto.FCMToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String title;
    private String body;
    private String token;
    private String notificationType;
}