package com.tridev.geoSphere.entities.sql;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "AppUpdateRequirements")
@Getter
@Setter
public class AppUpdateRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequirementId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PlatformId", nullable = false)
    private AppPlatform platform;

    @Column(name = "MinForceUpdateVersion", nullable = false)
    private String minForceUpdateVersion;

    @Column(name = "MinOptionalUpdateVersion", nullable = false)
    private String minOptionalUpdateVersion;

    @Column(name = "EffectiveDate", nullable = false)
    private LocalDateTime effectiveDate;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}
