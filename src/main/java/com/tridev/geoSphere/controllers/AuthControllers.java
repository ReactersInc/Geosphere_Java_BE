package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.authentications.RegisterUserDTO;
import com.tridev.geoSphere.dto.authentications.ResendOTPDTO;
import com.tridev.geoSphere.dto.authentications.VerifyEmailDTO;
import com.tridev.geoSphere.enums.UserType;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.UserService;
import com.tridev.geoSphere.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
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
