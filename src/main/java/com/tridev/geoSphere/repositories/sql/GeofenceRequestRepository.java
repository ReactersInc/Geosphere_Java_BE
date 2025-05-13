package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.GeofenceRequestEntity;
import com.tridev.geoSphere.enums.InvitationStatus;
import com.tridev.geoSphere.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeofenceRequestRepository extends JpaRepository<GeofenceRequestEntity, Long> {

    Optional<GeofenceRequestEntity> findByIdAndStatus(Long id, Integer status);

    Optional<GeofenceRequestEntity> findByUserIdAndRecipientEmailAndGeofenceIdAndStatus(Long requesterUserId, String email, Long geofenceId, Integer status);


    Page<GeofenceRequestEntity> findByRecipientEmailAndStatus(String email, Integer invitationStatus, Pageable pageable);
}
