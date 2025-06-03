package com.tridev.geoSphere.dto.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHardwareResponse {
    private Long totalHardware;
    private List<HardwareProjection> hardwareList;
}
