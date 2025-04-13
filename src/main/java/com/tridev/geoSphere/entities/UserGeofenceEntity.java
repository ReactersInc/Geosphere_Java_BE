package com.tridev.geoSphere.entities;

import jakarta.persistence.*;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserGeofence")
public class UserGeofenceEntity extends BaseEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Nonnull
    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @Nonnull
    @Column(name = "GeofenceId", nullable = false)
    private Integer geofenceId;

    @Column(name = "Status", columnDefinition = "INT DEFAULT 1")
    private Integer status;
}
