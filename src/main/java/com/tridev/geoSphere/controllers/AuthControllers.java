package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.DTO.RegisterUserDTO;
import com.tridev.geoSphere.DTO.VerifyEmailDTO;
import com.tridev.geoSphere.enums.UserType;
import com.tridev.geoSphere.services.UserService;
import com.tridev.geoSphere.services.VerifyOTPService;
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
    private VerifyOTPService verifyOTPService;




    @PostMapping("/register-user")
    public ResponseEntity<?>  registerUser(@RequestBody RegisterUserDTO registerUserDTO){
       String  userType= (UserType.USER.getDescription());
        return  registerUserService.registerUser(registerUserDTO, userType);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterUserDTO registerUserDTO){
        String  userType= (UserType.USER.getDescription());
        return  registerUserService.registerUser(registerUserDTO, userType);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyotp(@RequestBody VerifyEmailDTO verifyEmailDTO) throws Exception {

        Boolean isVerified = verifyOTPService.verifyOtp(verifyEmailDTO);

        if (isVerified){
            return new ResponseEntity<>("Verified", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }


    }





}
