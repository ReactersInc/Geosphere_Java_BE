package com.tridev.geoSphere.dto.Geofence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean enableNotifications;
    private Integer alertCount;
    private Integer status;
    private List<Coordinates> coordinates;
    private String colors;
    private LocalDateTime createdAt;
}