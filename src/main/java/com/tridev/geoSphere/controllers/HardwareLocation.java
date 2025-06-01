package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.hardware.HardwareLocationDTO;
import com.tridev.geoSphere.dto.location.LocationUpdateRequest;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.HardwareLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hard-location")
@RequiredArgsConstructor
public class HardwareLocation {

    @Autowired
    HardwareLocationService hardwareLocationService;

    @PostMapping
    public BaseResponse updateHardwareLocation(@RequestBody HardwareLocationDTO hardwareLocationDTO) throws Exception {
        return  hardwareLocationService.updateHardwareLocation(hardwareLocationDTO);
    }
}
