package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.DTO.RegisterUserDTO;
import com.tridev.geoSphere.DTO.ResendOTPDTO;
import com.tridev.geoSphere.DTO.VerifyEmailDTO;
import com.tridev.geoSphere.common.BaseResponse;
import com.tridev.geoSphere.enums.UserType;
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
    public ResponseEntity<BaseResponse> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        RegisterUserDTO registeredUser = registerUserService.registerUser(registerUserDTO, UserType.USER.getDescription());

        return ResponseBuilder.custom(registeredUser, HttpStatus.CREATED, "User registered successfully");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<BaseResponse> registerAdmin(@RequestBody RegisterUserDTO registerUserDTO) {
        RegisterUserDTO registeredUser = registerUserService.registerUser(registerUserDTO, UserType.ADMIN.getDescription());

        return ResponseBuilder.custom(registeredUser, HttpStatus.CREATED, "User registered successfully");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<BaseResponse> verifyotp(@RequestBody VerifyEmailDTO verifyEmailDTO) throws Exception {

        Boolean isVerified = verifyOTPService.verifyOtp(verifyEmailDTO);

        if (isVerified){
            return  ResponseBuilder.custom(true, HttpStatus.OK, "User Sucessfully verified");
        }else{
            return ResponseBuilder.custom(false, HttpStatus.BAD_REQUEST, "Failed to verify user verified");
        }


    }


    @PostMapping("/resend-otp")
    public ResponseEntity<BaseResponse> resendOTP(@RequestBody ResendOTPDTO email){

        Boolean isTrue = otpService.resendOtp(email);

        if(isTrue){
            return ResponseBuilder.custom(true, HttpStatus.OK,"OTP sent Sucessfully");
        }else{
            return ResponseBuilder.custom(false, HttpStatus.NOT_FOUND, "User Not found");
        }

    }





}
