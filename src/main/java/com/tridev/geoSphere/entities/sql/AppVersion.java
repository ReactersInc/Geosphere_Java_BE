package com.tridev.geoSphere.entities.sql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AppVersions")
public class AppVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppVersionId")
    private Long appVersionId;

    @Column(name = "CustomerId")
    private Long customerId;

    @Column(name = "UserRef")
    private Long userRef;

    @Column(name = "PlatformId")
    private Integer platformId;

    @Column(name = "AppVersion")
    private String appVersion;

    @Column(name = "InstalledAt")
    private LocalDateTime installedAt;
}