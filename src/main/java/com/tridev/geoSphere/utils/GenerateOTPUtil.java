package com.tridev.geoSphere.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateOTPUtil {


    public String OTP(){
        Random random = new Random();

        int otp= 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }
}
