package com.tridev.geoSphere.dto.FCMToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreTokenDTO {

    private Integer userId;

    private String deviceType;

    private String token;


    private String deviceId;






}
