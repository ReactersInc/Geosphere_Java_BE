package com.tridev.geoSphere.entities;

import com.tridev.geoSphere.enums.InvitationStatus;
import com.tridev.geoSphere.enums.NotificationStatus;
import com.tridev.geoSphere.enums.ResponseStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GeofenceRequest")
public class GeofenceRequestEntity extends BaseEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "GeofenceId", nullable = false)
    private Long geofenceId;

    @Column(name = "UserId", nullable = false)
    private Long userId;

    @Column(name = "RecipientEmail", nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private InvitationStatus status = InvitationStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "ResponseStatus")
    private ResponseStatus responseStatus = ResponseStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "NotificationStatus")
    private NotificationStatus notificationStatus = NotificationStatus.PENDING;


}
