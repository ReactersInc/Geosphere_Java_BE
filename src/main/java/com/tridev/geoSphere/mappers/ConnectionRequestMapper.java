package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.connectionRequest.ConnectionRequestDTO;
import com.tridev.geoSphere.dto.connectionRequest.ConnectionResponseDTO;
import com.tridev.geoSphere.entities.sql.ConnectionRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConnectionRequestMapper {

    ConnectionRequestEntity toEntity(ConnectionRequestDTO connectionRequestDTO);

    ConnectionRequestDTO toRequestDTO(ConnectionRequestEntity connectionRequestEntity);

    ConnectionResponseDTO toResponseDTO(ConnectionRequestEntity connectionRequestEntity);




    ConnectionRequestEntity toEntityFromResponse(ConnectionResponseDTO connectionResponeDTO);

}
