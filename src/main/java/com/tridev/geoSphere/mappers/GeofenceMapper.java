package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.Geofence.GeofenceResponse;
import com.tridev.geoSphere.entities.GeofenceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeofenceMapper {
    GeofenceResponse entityToResponse(GeofenceEntity entity);
}