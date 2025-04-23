package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.entities.UserGeofenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGeofenceRepo extends JpaRepository<UserGeofenceEntity, Long> {


//    Optional<UserGeofenceEntity> getByUserIdAndGeoFenceIdAndStatusNot(Long userId, Long geofenceId, Integer status);

    Optional<UserGeofenceEntity> findByUserIdAndGeofenceIdAndStatus(Long userId, Long geofenceId, Integer status);

    @Query(value = "SELECT u.* FROM Users u " +
            "INNER JOIN UserGeofence ug ON u.Id = ug.UserId " +
            "WHERE ug.GeofenceId = :geofenceId AND ug.Status = 1 AND u.Status = 1 " +
            "ORDER BY u.CreatedAt DESC",
            countQuery = "SELECT COUNT(u.Id) FROM Users u " +
                    "INNER JOIN UserGeofence ug ON u.Id = ug.UserId " +
                    "WHERE ug.GeofenceId = :geofenceId AND ug.Status = 1 AND u.Status = 1",
            nativeQuery = true)
    Page<UserEntity> findUsersByGeofenceId(@Param("geofenceId") Long geofenceId, Pageable pageable);






}