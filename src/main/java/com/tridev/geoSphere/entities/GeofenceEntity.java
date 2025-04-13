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
@Table(name = "Geofence")
public class GeofenceEntity extends BaseEntity{

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Nonnull
    @Column(name = "Name", nullable = false)
    private String name;

    @Nonnull
    @Lob
    @Column(name = "Coordinates", nullable = false)
    private String coordinates;

    @Column(name = "Status")
    private Integer status;
}
