package com.tridev.geoSphere.entities.sql;

import com.tridev.geoSphere.enums.NotificationStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notification")
public class NotificationEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Nonnull
    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @Nonnull
    @Column(name = "FCMTokenId", nullable = false)
    private Long fcmTokenId;

    @Nonnull
    @Column(name = "NotificationType", nullable = false)
    private String notificationType;

    @Nonnull
    @Column(name = "Title", nullable = false)
    private String title;

    @Nonnull
    @Column(name = "Message", nullable = false)
    private String message;

    @Column(name = "Status")
    private String status = NotificationStatus.PENDING.getValue();

    @Column(name = "SentAt")
    private LocalDateTime sentAt;

    @Column(name = "SentBy")
    private Integer sentBy;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
}
