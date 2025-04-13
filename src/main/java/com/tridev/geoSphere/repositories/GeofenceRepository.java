package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.GeofenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeofenceRepository extends JpaRepository<GeofenceEntity, Integer> {
    Page<GeofenceEntity> findAll(Pageable pageable);

    Page<GeofenceEntity> findByStatus(int value, PageRequest pageable);
    boolean existsByNameAndCreatedByAndStatusNot(String name, Long createdBy, Integer status);
    Optional<GeofenceEntity> findByNameAndCreatedBy(String name, Long createdBy);
}