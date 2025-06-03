package com.tridev.geoSphere.dto.hardware;

import java.time.LocalDateTime;

public interface HardwareProjection {
    Long getId();
    String getModel();
    String getManufacturer();
    String getMacAddress();
    Integer getStatusId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}