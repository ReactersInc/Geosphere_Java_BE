package com.tridev.geoSphere.dto.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareLocationDTO {
    private Double latitude;
    private Double longitude;
    private Double speed; // Optional
    private Double heading; // Optional
    private String deviceInfo; // Optional
    private String macAddress; // Optional, for hardware identification
    private Integer batteryLevel; // Optional, battery level percentage
}
