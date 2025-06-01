package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.AppPlatform;
import com.tridev.geoSphere.entities.sql.AppUpdateRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUpdateRequirementRepository extends JpaRepository<AppUpdateRequirement, Long> {
    Optional<AppUpdateRequirement> findByPlatform(AppPlatform platform);
}

