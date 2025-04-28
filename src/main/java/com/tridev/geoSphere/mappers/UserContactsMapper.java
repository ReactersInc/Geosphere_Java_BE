package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.userContacts.UserContactsRequestDTO;
import com.tridev.geoSphere.dto.userContacts.UserContactsResponseDTO;
import com.tridev.geoSphere.entities.sql.UserContactsEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserContactsMapper {

    UserContactsEntity toEntity(UserContactsRequestDTO userContactsRequestDTO);

    UserContactsResponseDTO toResponseDTO(UserEntity userEntity);
}
