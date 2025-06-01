package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.HardwareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareRepository extends JpaRepository<HardwareEntity, Long> {
    Boolean existsByMacAddressAndStatusId(String macAddress, Integer value);
    HardwareEntity findByIdAndStatusId(Long hardwareId, Integer value);
}
