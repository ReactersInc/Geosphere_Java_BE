package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.VerifyEmailDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
import com.tridev.geoSphere.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerifyOTPService {

    @Autowired
    private UserRepo userRepo;


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


}
