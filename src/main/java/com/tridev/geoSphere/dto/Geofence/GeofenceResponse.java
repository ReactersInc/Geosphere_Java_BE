package com.tridev.geoSphere.dto.Geofence;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GeofenceResponse {
    private Integer id;
    private String name;
    private String coordinates;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}