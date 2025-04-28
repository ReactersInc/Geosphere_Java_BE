package com.tridev.geoSphere.entities.sql;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FCMToken")
public class FCMTokenEntity extends BaseEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "UserId")
    private Long userId;

    @Column(name = "DeviceType")
    private String deviceType;

    @Column(name = "Token")
    private String token;

    @Column(name = "DeviceId")
    private String deviceId;

    @Column(name = "IsLoggedIn")
    private Boolean isLoggedIn = false;
}
