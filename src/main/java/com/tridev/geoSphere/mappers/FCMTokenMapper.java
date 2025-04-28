package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.entities.sql.FCMTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FCMTokenMapper {

    FCMTokenEntity toEntity(StoreTokenDTO storeTokenDTO);

    StoreTokenDTO toDTO(FCMTokenEntity fcmTokenEntity);






}
