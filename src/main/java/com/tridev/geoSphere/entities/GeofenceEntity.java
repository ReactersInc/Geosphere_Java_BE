package com.tridev.geoSphere.entities;

import jakarta.persistence.*;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Geofence")
public class GeofenceEntity extends  BaseEntity{

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Nonnull
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Nonnull
    @Lob
    @Column(name = "Coordinates", nullable = false)
    private String coordinates;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "EnableNotifications")
    private Boolean enableNotifications;

    @Column(name = "Colors")
    private String colors;

    @Column(name = "AlertCount")
    private Integer alertCount;

}
