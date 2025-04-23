package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.connectionRequest.ConnectionRequestDTO;
import com.tridev.geoSphere.dto.connectionRequest.ConnectionResponseDTO;
import com.tridev.geoSphere.entities.ConnectionRequestEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ConnectionRequestMapper {

    ConnectionRequestEntity toEntity(ConnectionRequestDTO connectionRequestDTO);

    ConnectionRequestDTO toRequestDTO(ConnectionRequestEntity connectionRequestEntity);

    ConnectionResponseDTO toResponseDTO(ConnectionRequestEntity connectionRequestEntity);




    ConnectionRequestEntity toEntityFromResponse(ConnectionResponseDTO connectionResponeDTO);

}
