package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.GeofenceRequestEntity;
import com.tridev.geoSphere.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeofenceRequestRepository extends JpaRepository<GeofenceRequestEntity, Long> {

    Optional<GeofenceRequestEntity> findByIdAndStatus(Long id, InvitationStatus status);

    Optional<GeofenceRequestEntity> findByUserIdAndRecipientEmailAndGeofenceIdAndStatus(Long requesterUserId, String email, Long geofenceId, InvitationStatus status);


}
