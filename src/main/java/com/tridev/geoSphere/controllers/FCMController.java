package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.FCMToken.NotificationDTO;
import com.tridev.geoSphere.dto.FCMToken.StoreTokenDTO;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.FcmTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class FCMController {


    @Autowired
    private FcmTokenService fcmTokenService;


    @PostMapping("/fcm-token")
    public BaseResponse storeToken(@RequestBody StoreTokenDTO storeTokenDTO){
        return  fcmTokenService.storeToken(storeTokenDTO);
    }

    @PostMapping
    public BaseResponse sendNotification(@RequestBody NotificationDTO notificationDTO) throws BadRequestException {
        return fcmTokenService.sendNotification(notificationDTO.getToken(), notificationDTO.getTitle(), notificationDTO.getBody(),notificationDTO.getNotificationType());
    }


    @GetMapping("/user-notifications")
    BaseResponse fetchUserNotification(@RequestParam(name = "pageNo", defaultValue = "0") String pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") String pageSize,
                                       @RequestParam(name = "sortOrder", defaultValue = "DESC") String sortOrder) throws BadRequestException {
        return fcmTokenService.fetchUserNotification( Integer.parseInt(pageNo), Integer.parseInt(pageSize), sortOrder);
    }
}
