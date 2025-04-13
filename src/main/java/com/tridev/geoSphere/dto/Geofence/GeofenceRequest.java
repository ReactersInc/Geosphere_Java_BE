package com.tridev.geoSphere.dto.Geofence;

import lombok.Data;

@Data
public class GeofenceRequest {
    private String name;
    private String coordinates;
    private Integer status;
    private Long createdBy;
}
