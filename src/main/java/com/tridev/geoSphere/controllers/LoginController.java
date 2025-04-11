package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.DTO.LoginDTO;
import com.tridev.geoSphere.DTO.LoginResponseDTO;
import com.tridev.geoSphere.common.BaseResponse;
import com.tridev.geoSphere.services.LoginUserService;
import com.tridev.geoSphere.services.UserServiceDetailsImpl;
import com.tridev.geoSphere.utils.JwtUtil;
import com.tridev.geoSphere.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<BaseResponse> loginUser(@RequestBody LoginDTO data){
        LoginResponseDTO response = loginUserService.loginUser(data);

        if(response == null){
            return ResponseBuilder.custom(null, HttpStatus.NOT_FOUND, "User not found");
        }else{
        return ResponseBuilder.custom(response, HttpStatus.OK,"User Logged in successfully");
        }



    }
}
