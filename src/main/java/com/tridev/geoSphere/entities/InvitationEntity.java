package com.tridev.geoSphere.entities;

import com.tridev.geoSphere.enums.InvitationStatus;
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
@Table(name = "Invitation")
public class InvitationEntity extends BaseEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "GeofenceId", nullable = false)
    private Integer geofenceId;

    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @Column(name = "RecipientEmail", nullable = false, length = 255)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private InvitationStatus status = InvitationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "ResponseStatus")
    private ResponseStatus responseStatus = ResponseStatus.PENDING;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "SentAt")
    private LocalDateTime sentAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "UpdatedBy")
    private Integer updatedBy;
}
