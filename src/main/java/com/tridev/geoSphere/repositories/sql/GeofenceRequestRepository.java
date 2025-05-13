package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.GeofenceRequestEntity;
import com.tridev.geoSphere.enums.InvitationStatus;
import com.tridev.geoSphere.enums.ResponseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeofenceRequestRepository extends JpaRepository<GeofenceRequestEntity, Long> {

    Optional<GeofenceRequestEntity> findByIdAndStatus(Long id, InvitationStatus status);

    Optional<GeofenceRequestEntity> findByUserIdAndRecipientEmailAndGeofenceIdAndStatus(Long requesterUserId, String email, Long geofenceId, InvitationStatus status);


    Integer countByUserIdAndResponseStatus(Long userId, ResponseStatus responseStatus);

    List<GeofenceRequestEntity> findAllByUserId(Long userId);
}
