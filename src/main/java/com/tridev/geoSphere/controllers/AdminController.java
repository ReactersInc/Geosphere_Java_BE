package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public BaseResponse getAllUsers(){
        return adminService.getAllUsers();
    }
}
