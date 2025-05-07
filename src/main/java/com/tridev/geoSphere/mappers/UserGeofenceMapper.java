package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceRequestDTO;
import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceResponseDTO;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGeofenceMapper {

    UserGeofenceEntity toEntity(UserGeofeceRequestDTO userGeofeceRequestDTO);

    UserGeofeceResponseDTO toDTO(UserGeofenceEntity userGeofenceEntity);

    @Mapping(target = "locationExists", ignore = true)
    @Mapping(target = "currentLocation", ignore = true)
    @Mapping(target = "lastLocationUpdateTime", ignore = true)
    @Mapping(target = "speed", ignore = true)
    @Mapping(target = "heading", ignore = true)
    UserGeofeceResponseDTO toResponseDTO(UserEntity userEntity);



}
