package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import com.tridev.geoSphere.entities.sql.UserHardwareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHardwareRepository extends JpaRepository<UserHardwareEntity, Long> {
    Boolean existsByUser_IdAndHardware_IdAndStatusId(Long userId, Long hardwareId, Integer statusId);
    List<UserHardwareEntity> findAllByUser_IdAndStatusId(Long l, Integer value);
}
