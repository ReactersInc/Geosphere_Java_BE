package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.RegisterUserDTO;
import com.tridev.geoSphere.dto.ResendOTPDTO;
import com.tridev.geoSphere.dto.VerifyEmailDTO;
import com.tridev.geoSphere.enums.UserType;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.UserService;
import com.tridev.geoSphere.services.OTPService;
import com.tridev.geoSphere.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthControllers {

    @Autowired
    private UserService registerUserService;

    @Autowired
    private OTPService verifyOTPService;


    @Autowired
    private OTPService otpService;




    @PostMapping("/register-user")
    public BaseResponse registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return registerUserService.registerUser(registerUserDTO, UserType.USER.getDescription());
    }


    @PostMapping("/register-admin")
    public BaseResponse registerAdmin(@RequestBody RegisterUserDTO registerUserDTO) {
        return registerUserService.registerUser(registerUserDTO, UserType.ADMIN.getDescription());
    }



    @PostMapping("/verify-otp")
    public BaseResponse verifyOtp(@RequestBody VerifyEmailDTO verifyEmailDTO) throws Exception {
        return verifyOTPService.verifyOtp(verifyEmailDTO);
    }



    @PostMapping("/resend-otp")
    public BaseResponse resendOTP(@RequestBody ResendOTPDTO email) throws Exception {
        return otpService.resendOtp(email);
    }






}
