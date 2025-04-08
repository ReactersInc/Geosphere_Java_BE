package com.tridev.geoSphere.services;

import com.tridev.geoSphere.DTO.LoginDTO;
import com.tridev.geoSphere.entities.RegisterUserEntity;
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



    public String loginUser(LoginDTO data) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
            );

            RegisterUserEntity user = registerUserRepo.findByEmail(data.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String role = user.getRole().toString(); // assuming it's a String, or use user.getRole().name() if it's enum

            String jwt = jwtUtil.generateToken(user.getEmail(), role);

            return jwt;

        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials", e);
        }
    }

}
