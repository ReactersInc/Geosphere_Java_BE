package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.AppPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppPlatformRepository extends JpaRepository<AppPlatform, Long> {
    Optional<AppPlatform> findByPlatformName(String platformName);
}