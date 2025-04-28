package com.tridev.geoSphere.utils;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepo registerUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
        UserEntity user = null;
            user = registerUserRepo.findByEmailAndStatus(email, Status.ACTIVE.getValue())
                    .orElseThrow(() -> new BadRequestException(CommonValidationConstant.USER_NOT_FOUND));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole())) // or use authorities if needed
                .build();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }




}
