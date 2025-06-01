package com.tridev.geoSphere.dto.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddHardwareRequest {
    private String model;
    private String manufacturer;
    private String macAddress;
}