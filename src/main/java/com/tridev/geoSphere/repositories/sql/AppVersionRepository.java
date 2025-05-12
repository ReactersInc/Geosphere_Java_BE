package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
}
