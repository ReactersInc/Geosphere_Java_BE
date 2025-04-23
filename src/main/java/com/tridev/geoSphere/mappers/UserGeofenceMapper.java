package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceRequestDTO;
import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceResponseDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.entities.UserGeofenceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGeofenceMapper {

    UserGeofenceEntity toEntity(UserGeofeceRequestDTO userGeofeceRequestDTO);

    UserGeofeceResponseDTO toDTO(UserGeofenceEntity userGeofenceEntity);

    UserGeofeceResponseDTO toResponseDTO(UserEntity userEntity);
}
