package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMTokenEntity, Long> {

    Optional<FCMTokenEntity> findByUserIdAndDeviceId(Long userId, String deviceId);

    boolean existsByUserId(Long userId);
}
