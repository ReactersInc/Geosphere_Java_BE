package com.tridev.geoSphere.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {
    private double latitude;
    private double longitude;
    private Double speed; // Optional
    private Double heading; // Optional
    private String deviceInfo; // Optional
}