package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.GeofenceRequest.GeofenceRequestDTO;
import com.tridev.geoSphere.entities.sql.GeofenceRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeofenceRequestMapper {

    GeofenceRequestEntity toEntity(GeofenceRequestDTO geofenceRequestDTO);

    GeofenceRequestDTO toDTO(GeofenceRequestEntity geofenceRequestEntity);
//    GeofenceRequestDTO toDTO(GeofenceRequestEntity geofenceRequestEntity);
}
