package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.AppUpdate.UpdateCheckResponse;
import com.tridev.geoSphere.services.AppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0.0/app-update")
public class AppUpdateController {

    @Autowired
    private AppUpdateService appUpdateService;

    @GetMapping("/check")
    public UpdateCheckResponse checkForUpdate(
            @RequestParam Long platformId,
            @RequestParam String currentAppVersion) {
        return appUpdateService.checkForUpdate(platformId, currentAppVersion);
    }
}
