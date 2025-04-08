package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.DTO.LoginDTO;
import com.tridev.geoSphere.services.LoginUserService;
import com.tridev.geoSphere.services.UserServiceDetailsImpl;
import com.tridev.geoSphere.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")

public class LoginController {

    @Autowired
    private UserServiceDetailsImpl userServiceDetails;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginUserService loginUserService;


    @PostMapping
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO data){
        String response = loginUserService.loginUser(data);

        return new ResponseEntity<>(response, HttpStatus.OK);


    }
}
