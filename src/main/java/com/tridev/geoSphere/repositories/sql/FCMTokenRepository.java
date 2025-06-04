package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMTokenEntity, Long> {

    Optional<FCMTokenEntity> findByUserIdAndDeviceId(Long userId, String deviceId);

    Optional<FCMTokenEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    List<FCMTokenEntity> findAllByToken(String token);
}
