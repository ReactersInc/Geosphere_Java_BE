package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {


    @Autowired
    private ProfileService profileService;


    @GetMapping
    public BaseResponse getProfile() throws BadRequestException {

        return profileService.findUserByUserId();

    }
}
