package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.authentications.LoginDTO;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.LoginUserService;
import com.tridev.geoSphere.utils.UserServiceDetailsImpl;
import com.tridev.geoSphere.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse loginUser(@RequestBody LoginDTO data)throws Exception{
        return loginUserService.loginUser(data);
    }
}
