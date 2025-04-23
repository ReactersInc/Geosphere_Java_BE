package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.User.PublicProfileResponseDTO;
import com.tridev.geoSphere.dto.common.PaginatedResponse;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
public class PublicProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public BaseResponse getPublicProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction)  throws Exception {

        return userService.getPublicProfilesWithStatus(page, size, sortBy, direction);
    }
}
