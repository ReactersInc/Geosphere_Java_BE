package com.tridev.geoSphere.mappers;


import com.tridev.geoSphere.DTO.RegisterUserDTO;
import com.tridev.geoSphere.DTO.UserDetailsDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

RegisterUserDTO toDTO(RegisterUserEntity registerUser);


RegisterUserEntity toEntity(RegisterUserDTO registerUserDTO);



List<UserDetailsDTO> toEntities(List<RegisterUserEntity> registerUserEntity);


}

