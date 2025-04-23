package com.tridev.geoSphere.services;

import com.tridev.geoSphere.config.ValidationFailureException;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.constant.ErrorConstants;
import com.tridev.geoSphere.dto.authentications.LoginDTO;
import com.tridev.geoSphere.dto.authentications.LoginResponseDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import com.tridev.geoSphere.utils.UserServiceDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class LoginUserService {

    @Autowired
    private UserRepo registerUserRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceDetailsImpl userServiceDetails;




    public BaseResponse loginUser(LoginDTO data) throws Exception {
        log.info("login user");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
            );
            UserEntity user = registerUserRepo.findByEmailAndStatus(data.getEmail(), Status.ACTIVE.getValue())
                    .orElseThrow(() -> new BadCredentialsException(CommonValidationConstant.USER_NOT_FOUND));
            log.info("user:{}", user);

            if (Boolean.TRUE.equals(user.getIsVerified())) {
                String jwt = jwtUtil.generateToken(
                        user.getEmail(),
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName()
                );
                LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
                loginResponseDTO.setToken(jwt);
                return GeosphereServiceUtility.getBaseResponse(loginResponseDTO);
            } else {
                throw new BadRequestException(CommonValidationConstant.USER_NOT_VERIFIED);
            }

        } catch (Exception e) {
            log.error("Error during authentication: {}", e.getMessage());
                throw new ValidationFailureException(ErrorConstants.INVALID_CREDENTIALS, ErrorConstants.INVALID_CREDENTIALS_DESC, 400);
          }
    }



}





