package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.LoginDTO;
import com.tridev.geoSphere.DTO.LoginResponseDTO;
import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.repositories.UserRepo;
import com.tridev.geoSphere.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

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


    public LoginResponseDTO loginUser(LoginDTO data) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
            );

            UserEntity user = registerUserRepo.findByEmail(data.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (Boolean.TRUE.equals(user.getIsVerified())) {
                String jwt = jwtUtil.generateToken(
                        user.getEmail(),
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName()
                );

                return new LoginResponseDTO(jwt);
            } else {
                return null; // will be handled in controller
            }

        } catch (Exception e) {
            // Optional: log exception
            return null; // Let controller handle response for failure
        }
    }



}





