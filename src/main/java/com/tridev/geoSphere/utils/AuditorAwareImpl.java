package com.tridev.geoSphere.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            Long userId = jwtUtil.getUserIdFromToken(); // implement this method in JwtUtil
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}