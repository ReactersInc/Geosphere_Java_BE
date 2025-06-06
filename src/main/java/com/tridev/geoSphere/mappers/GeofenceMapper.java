package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.Geofence.GeofenceResponseDTO;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;
import com.tridev.geoSphere.utils.GeofenceConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , imports = GeofenceConversionUtil.class)
public interface GeofenceMapper {
    @Mapping(target = "coordinates", expression = "java(GeofenceConversionUtil.parseCoordinates(entity.getCoordinates()))")
    @Mapping(target = "colors", expression = "java(GeofenceConversionUtil.extractColors(entity.getColors()))")
    GeofenceResponseDTO entityToResponse(GeofenceEntity entity);
}