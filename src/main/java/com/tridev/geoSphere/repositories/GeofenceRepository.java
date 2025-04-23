package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.GeofenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeofenceRepository extends JpaRepository<GeofenceEntity, Long> {
    Page<GeofenceEntity> findAll(Pageable pageable);

    Optional<GeofenceEntity> findByIdAndStatus(Long id, Integer status);

    Page<GeofenceEntity> findByStatus(Integer value, PageRequest pageable);
    boolean existsByNameAndCreatedByAndStatusNot(String name, Long userId, Integer status);
    Optional<GeofenceEntity> findByNameAndCreatedBy(String name, Long createdBy);


    Optional<GeofenceEntity> findByIdAndCreatedByAndStatusNot(Long geofenceId, Long userId, Integer status);

    Page<GeofenceEntity> findByCreatedByAndStatusNot(Long createdBy, int status, Pageable pageable);





}