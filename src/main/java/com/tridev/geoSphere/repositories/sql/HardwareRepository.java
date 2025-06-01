package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.HardwareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HardwareRepository extends JpaRepository<HardwareEntity, Long> {
    Boolean existsByMacAddressAndStatusId(String macAddress, Integer value);
    HardwareEntity findByIdAndStatusId(Long hardwareId, Integer value);

    Optional<HardwareEntity> findByMacAddressAndStatusId(String macAddress, Integer value);
}
