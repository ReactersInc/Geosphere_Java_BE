package com.tridev.geoSphere.dto.hardware;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignHardwareRequest {
    private String hardwareMac;
    private Long userId;
    private Integer battery;
}