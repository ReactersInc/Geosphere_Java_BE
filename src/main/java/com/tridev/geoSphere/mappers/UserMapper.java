package com.tridev.geoSphere.mappers;


import com.tridev.geoSphere.dto.Profile.ProfileDTO;
import com.tridev.geoSphere.dto.User.PublicProfileResponseDTO;
import com.tridev.geoSphere.dto.authentications.RegisterUserDTO;
import com.tridev.geoSphere.dto.User.UserDetailsDTO;
import com.tridev.geoSphere.entities.sql.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {




RegisterUserDTO toDTO(UserEntity registerUser);


UserEntity toEntity(RegisterUserDTO registerUserDTO);



List<UserDetailsDTO> toEntities(List<UserEntity> registerUserEntity);



ProfileDTO toProfile(UserEntity userEntity);

    PublicProfileResponseDTO toPublicProfile(UserEntity userEntity);

    default List<PublicProfileResponseDTO> toPublicProfile(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(this::toPublicProfile)
                .collect(Collectors.toList());
    }


    PublicProfileResponseDTO toPublicProfileDTO(UserEntity user);
}

