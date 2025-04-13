package com.tridev.geoSphere.mappers;


import com.tridev.geoSphere.dto.RegisterUserDTO;
import com.tridev.geoSphere.dto.UserDetailsDTO;
import com.tridev.geoSphere.entities.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

RegisterUserDTO toDTO(UserEntity registerUser);


UserEntity toEntity(RegisterUserDTO registerUserDTO);



List<UserDetailsDTO> toEntities(List<UserEntity> registerUserEntity);


}

