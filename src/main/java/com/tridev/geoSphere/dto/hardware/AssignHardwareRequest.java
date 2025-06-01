package com.tridev.geoSphere.dto.hardware;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignHardwareRequest {
    private Long hardwareId;
    private Long userId;
    private Integer battery;
}