package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByUserId(Integer userId, Pageable pageable);
}