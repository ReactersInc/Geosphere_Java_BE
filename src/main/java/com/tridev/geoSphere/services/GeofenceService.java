package com.tridev.geoSphere.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.Geofence.*;
import com.tridev.geoSphere.entities.GeofenceEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.exceptions.ResourceNotFoundException;
import com.tridev.geoSphere.mappers.GeofenceMapper;
import com.tridev.geoSphere.repositories.GeofenceRepository;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeofenceService {

    private final GeofenceRepository geofenceRepository;
    private final GeofenceMapper geofenceMapper;

    private final JwtUtil jwtUtil;

    @Transactional
    public BaseResponse createGeofence(GeofenceRequest request) throws Exception {
//        validateGeofenceRequest(request);

        Long userId = jwtUtil.getUserIdFromToken();

        ObjectMapper objectMapper = new ObjectMapper();

        // Check if geofence with same name already exists for this user (and not deleted)
        if (geofenceRepository.existsByNameAndCreatedByAndStatusNot(
                request.getName(),
                userId,
                Status.DELETED.getValue())) {
            throw new BadRequestException("Geofence with this name already exists");
        }

        GeofenceEntity entity = new GeofenceEntity();


        entity.setName(request.getName());
//        entity.setCoordinates(request.getCoordinates());
        String coordinatesJson = objectMapper.writeValueAsString(request.getCoordinates());
        entity.setCoordinates(coordinatesJson);
         // Set createdBy from request
       entity.setStatus(Status.ACTIVE.getValue());
        entity.setDescription(request.getDescription());
        entity.setEnableNotifications(request.getEnableNotifications());
        String colorsJson = objectMapper.writeValueAsString(request.getColors());
        entity.setColors(colorsJson);
        entity.setAlertCount(0);


        GeofenceEntity savedEntity = geofenceRepository.save(entity);
        return GeosphereServiceUtility.getBaseResponse(geofenceMapper.entityToResponse(savedEntity));
    }

//    public BaseResponse updateGeofence(Long id, GeofenceRequest request) throws Exception {
//        GeofenceEntity entity = geofenceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Geofence not found with id: " + id));
//
//        // Check if geofence is deleted
//        if (entity.getStatus() == Status.DELETED.getValue()) {
//            throw new BadRequestException("Cannot update a deleted geofence");
//        }
//
//        validateGeofenceRequest(request);
//
//        // Check if another geofence with the new name already exists (excluding current one)
//        Optional<GeofenceEntity> existingGeofence = geofenceRepository.findByNameAndCreatedBy(
//                request.getName(),
//                entity.getCreatedBy());
//
//        if (existingGeofence.isPresent() && !existingGeofence.get().getId().equals(id)) {
//            throw new BadRequestException("Geofence with this name already exists");
//        }
//
//        entity.setName(request.getName());
//        entity.setCoordinates(request.getCoordinates());
//
//        // Only update status if provided in request, otherwise keep existing
//        if (request.getStatus() != null) {
//            entity.setStatus(request.getStatus());
//        }
//
//        entity.setUpdatedAt(LocalDateTime.now());
//
//        GeofenceEntity updatedEntity = geofenceRepository.save(entity);
//        return GeosphereServiceUtility.getBaseResponse(geofenceMapper.entityToResponse(updatedEntity));
//    }
//
//    public BaseResponse deleteGeofence(Long id) {
//        GeofenceEntity entity = geofenceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Geofence not found with id: " + id));
//
//        // Soft delete by updating status
//        entity.setStatus(Status.DELETED.getValue());
//        geofenceRepository.save(entity);
//
//        return GeosphereServiceUtility.getBaseResponseWithoutData();
//    }
//
//    public BaseResponse getGeofenceById(Long id) {
//        GeofenceEntity entity = geofenceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Geofence not found with id: " + id));
//
//        return GeosphereServiceUtility.getBaseResponse(geofenceMapper.entityToResponse(entity));
//    }
//
//    public BaseResponse getAllGeofences(int pageNo, int pageSize, String sortOrder) throws Exception{
//        validatePaginationParams(pageNo, pageSize);
//
//        Sort.Direction direction = sortOrder.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        PageRequest pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, "id"));
//
//        // Only fetch active geofences (status != DELETED)
//        Page<GeofenceEntity> page = geofenceRepository.findByStatus(
//                Status.ACTIVE.getValue(),
//                pageable
//        );
//
//        GeofencePaginationResponse response = new GeofencePaginationResponse();
//        response.setTotalPages(page.getTotalPages());
//        response.setPageNo(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalRecords(page.getTotalElements());
//        response.setSortOrder(sortOrder);
//        response.setData(page.getContent().stream()
//                .map(geofenceMapper::entityToResponse)
//                .collect(Collectors.toList()));
//
//        return GeosphereServiceUtility.getBaseResponse(response);
//    }
//
//
//    private void validateGeofenceRequest(GeofenceRequest request)  throws Exception{
//        if (request.getName() == null || request.getName().trim().isEmpty()) {
//            throw new BadRequestException("Geofence name cannot be empty");
//        }
//        if (request.getCoordinates() == null || request.getCoordinates().trim().isEmpty()) {
//            throw new BadRequestException("Coordinates cannot be empty");
//        }
//    }
//
//    private void validatePaginationParams(int pageNo, int pageSize) throws Exception {
//        if (pageNo < 0) {
//            throw new BadRequestException("Page number cannot be negative");
//        }
//        if (pageSize < 1 || pageSize > 100) {
//            throw new BadRequestException("Page size must be between 1 and 100");
//        }
//    }


    @Transactional
    public BaseResponse updateGeofence( UpdateGeofenceRequest updateGeofenceRequest) throws Exception {
        Long userId = jwtUtil.getUserIdFromToken();

        GeofenceEntity entity = geofenceRepository.findByIdAndCreatedByAndStatusNot(
                        updateGeofenceRequest.getGeofenceId(),
                        userId,
                        Status.DELETED.getValue())
                .orElseThrow(() -> new BadRequestException(CommonValidationConstant.GEOFENCE_NOT_FOUND));

        if (!entity.getName().equals(updateGeofenceRequest.getName()) &&
                geofenceRepository.existsByNameAndCreatedByAndStatusNot(updateGeofenceRequest.getName(), userId, Status.DELETED.getValue())) {
            throw new BadRequestException(CommonValidationConstant.GEOFENCE_ALREADY_EXIST);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        entity.setName(updateGeofenceRequest.getName());
        entity.setDescription(updateGeofenceRequest.getDescription());
        entity.setEnableNotifications(updateGeofenceRequest.getEnableNotifications());
        entity.setCoordinates(objectMapper.writeValueAsString(updateGeofenceRequest.getCoordinates()));
        entity.setColors(objectMapper.writeValueAsString(updateGeofenceRequest.getColors()));

        GeofenceEntity updatedEntity = geofenceRepository.save(entity);
        return GeosphereServiceUtility.getBaseResponse(geofenceMapper.entityToResponse(updatedEntity));
    }

}