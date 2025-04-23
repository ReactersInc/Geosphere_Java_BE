package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.authentications.ResendOTPDTO;
import com.tridev.geoSphere.dto.authentications.VerifyEmailDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GenerateOTPUtil;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
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


    public BaseResponse verifyOtp(VerifyEmailDTO verifyEmailDTO) throws Exception {
        Optional<UserEntity> optionalUser = userRepo.findByEmailAndStatus(verifyEmailDTO.getEmail(), Status.PENDING.getValue());

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            String storedOtp = user.getOtp();

            if (storedOtp != null && storedOtp.equals(verifyEmailDTO.getOtp())) {
                user.setIsVerified(true);
                user.setStatus(Status.ACTIVE.getValue());
                userRepo.save(user);
                return GeosphereServiceUtility.getBaseResponseWithoutData();
            } else {
                throw new BadRequestException(CommonValidationConstant.INCORRECT_OTP);
            }
        } else {
            throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
        }
    }




    public BaseResponse resendOtp(ResendOTPDTO resendOTPDTO) throws Exception{
        Optional<UserEntity> optionalUser = userRepo.findByEmailAndStatus(resendOTPDTO.getEmail(), Status.PENDING.getValue());
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            String otp = generateOTPUtil.OTP();
            user.setOtp(otp);
            userRepo.save(user);
            Boolean email = emailService.sendEmail(user.getEmail(), "OTP to register on Geofence App", "Your OTP is " + otp);
            if(email){
                return GeosphereServiceUtility.getBaseResponseWithoutData();
            }
            else {
                throw new BadRequestException(CommonValidationConstant.EMAIL_NOT_SENT);
            }


        }else {
            throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
        }



    }



}
