package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.FcmTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fcm-token")
public class FCMController {


    @Autowired
    private FcmTokenService fcmTokenService;


    @PostMapping
    public BaseResponse storeToken(@RequestBody StoreTokenDTO storeTokenDTO){

        return  fcmTokenService.storeToken(storeTokenDTO);

    }
}
