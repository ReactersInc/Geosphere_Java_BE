package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.dto.connectionRequest.ConnectionResponseDTO;
import com.tridev.geoSphere.entities.sql.ConnectionRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequestEntity, Long > {

    List<ConnectionResponseDTO> findByRequesterUserIdAndStatus(Long requesterUserId, Integer status);
    Page<ConnectionRequestEntity> findByTargetUserIdAndStatus(Long targetUserId, Integer status, Pageable pageable);
    Optional<ConnectionRequestEntity> findByRequesterUserIdAndTargetUserIdAndStatus(Long requesterUserId, Long targetUserId, Integer status);
    List<ConnectionRequestEntity> findByRequesterUserIdAndTargetUserId(Long requesterUserId, Long targetUserId);
    List<ConnectionRequestEntity> findByTargetUserIdAndRequesterUserId(Long targetUserId, Long requesterUserId);


    Optional<ConnectionRequestEntity> findByIdAndStatus(Long Id, Integer status);

    List<ConnectionRequestEntity> findByRequesterUserId(Long requesterUserId);
}
