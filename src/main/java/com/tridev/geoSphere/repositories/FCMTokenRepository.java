package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.FCMTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMTokenRepository extends JpaRepository<FCMTokenEntity, Integer> {
}
