package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.hardware.AddHardwareRequest;
import com.tridev.geoSphere.dto.hardware.AssignHardwareRequest;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.HardwareService;
import com.tridev.geoSphere.services.UserHardwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hardware")
@Slf4j
public class HardwareController {

    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private UserHardwareService userHardwareService;

    @PostMapping
    public BaseResponse addHardware(@RequestBody AddHardwareRequest request)throws Exception {
        return hardwareService.addHardware(request);
    }

    @DeleteMapping("/{id}")
    public BaseResponse softDeleteHardware(@PathVariable String id)throws Exception {
        return hardwareService.softDeleteHardware(id);
    }

    @PostMapping("/user")
    public BaseResponse assignHardwareToUser(@RequestBody AssignHardwareRequest request) throws Exception {
        return userHardwareService.assignHardwareToUser(request);
    }

    @GetMapping("/user")
    public BaseResponse getUserHardware() {
        return userHardwareService.getHardware();
    }
}
