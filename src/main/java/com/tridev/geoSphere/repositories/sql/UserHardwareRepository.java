package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.dto.hardware.HardwareProjection;
import com.tridev.geoSphere.entities.sql.HardwareEntity;
import com.tridev.geoSphere.entities.sql.UserHardwareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserHardwareRepository extends JpaRepository<UserHardwareEntity, Long> {
    Boolean existsByUser_IdAndHardware_IdAndStatusId(Long userId, Long hardwareId, Integer statusId);
    List<UserHardwareEntity> findAllByUser_IdAndStatusId(Long l, Integer value);

    Optional<UserHardwareEntity> findByHardwareAndStatusId(HardwareEntity hardware, Integer value);
    @Query(value = "SELECT h.id, h.model, h.manufacturer, h.mac_address, h.status_id, h.created_at, h.updated_at " +
            "FROM user_hardware uh " +
            "JOIN hardware h ON uh.hardware_id = h.id " +
            "WHERE uh.user_id = :userId AND uh.status_id = :statusId", nativeQuery = true)
    List<HardwareProjection> findAllHardwareByUserIdAndStatusId(@Param("userId") Long userId, @Param("statusId") Integer statusId);
}