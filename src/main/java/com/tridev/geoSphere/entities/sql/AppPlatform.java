package com.tridev.geoSphere.entities.sql;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "AppPlatforms")
@Getter
@Setter
public class AppPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlatformId")
    private Long id;

    @Column(name = "PlatformName", unique = true, nullable = false)
    private String platformName;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}