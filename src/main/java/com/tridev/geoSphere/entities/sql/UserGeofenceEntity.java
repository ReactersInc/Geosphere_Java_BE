    package com.tridev.geoSphere.entities.sql;

    import jakarta.persistence.*;
    import jakarta.annotation.Nonnull;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

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
        private Long id;

        @Nonnull
        @Column(name = "UserId", nullable = false)
        private Long userId;

        @Nonnull
        @Column(name = "GeofenceId", nullable = false)
        private Long geofenceId;

        @Column(name = "Status", columnDefinition = "INT DEFAULT 1")
        private Integer status;

        @Column(name = "IsCurrentlyInside", columnDefinition = "BOOLEAN DEFAULT FALSE")
        private Boolean isCurrentlyInside = false;

        @Column(name = "LastEnteredAt")
        private Long lastEnteredAt;

        @Column(name = "LastExitedAt")
        private Long lastExitedAt;
    }
