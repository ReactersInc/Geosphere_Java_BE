package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.ResendOTPDTO;
import com.tridev.geoSphere.DTO.VerifyEmailDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.utils.GenerateOTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OTPService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenerateOTPUtil generateOTPUtil;

    @Autowired
    private EmailService emailService;


    public Boolean verifyOtp(VerifyEmailDTO verifyEmailDTO) throws Exception {
        Optional<RegisterUserEntity> optionalUser = userRepo.findByEmail(verifyEmailDTO.getEmail());

        if (optionalUser.isPresent()) {
            RegisterUserEntity user = optionalUser.get();
            String storedOtp = user.getOtp();

            if(storedOtp != null && storedOtp.equals(verifyEmailDTO.getOtp())){
                user.setIsVerified(true);
                userRepo.save(user);
                return true;
            }else{
                return false;
            }


        } else {
            throw new Exception("User not found");
        }
    }



    public Boolean resendOtp(ResendOTPDTO resendOTPDTO){

        Optional<RegisterUserEntity> optionalUser = userRepo.findByEmail(resendOTPDTO.getEmail());
        if(optionalUser.isPresent()){

            RegisterUserEntity user = optionalUser.get();
            String otp = generateOTPUtil.OTP();
            user.setOtp(otp);
            userRepo.save(user);

            emailService.sendEmail(user.getEmail(), "OTP to register on Geofence App", "Your OTP is " + otp);


            return true;



        }

        return false;

    }


}
