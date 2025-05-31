package com.tridev.geoSphere.services;

import com.tridev.geoSphere.config.ValidationFailureException;
import com.tridev.geoSphere.constant.CommonConstants;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.constant.ErrorConstants;
import com.tridev.geoSphere.dto.authentications.LoginDTO;
import com.tridev.geoSphere.dto.authentications.LoginResponseDTO;
import com.tridev.geoSphere.entities.sql.AppVersion;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.sql.AppVersionRepository;
import com.tridev.geoSphere.repositories.sql.UserRepo;
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
import org.springframework.util.ObjectUtils;

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

    @Autowired
    private AppVersionRepository appVersionRepository;


    public BaseResponse  loginUser(LoginDTO data) throws Exception {
        log.info("login user");
        try {
            if (data.getEmail() == null || data.getEmail().isBlank()) {
                throw new ValidationFailureException("EMAIL_REQUIRED", "Email is mandatory", 400);
            }
            if (!data.getEmail().matches(CommonConstants.EMAIL_REGEX)) {
                throw new ValidationFailureException("INVALID_EMAIL", "Email format is invalid", 400);
            }
            if (data.getPassword() == null || data.getPassword().isBlank()) {
                throw new ValidationFailureException("PASSWORD_REQUIRED", "Password is mandatory", 400);
            }

            UserEntity user = registerUserRepo.findByEmailAndStatus(data.getEmail(), Status.ACTIVE.getValue())
                    .orElseThrow(() -> new BadRequestException(CommonValidationConstant.USER_NOT_FOUND));

            log.info("user:{}", user);

            // Authenticate user
            log.info("Authenticating user with email: {}", data.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
            );
            log.info("User authenticated successfully");
            // Retrieve user entity

            // Generate JWT token
            String jwt = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName()
            );

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(jwt);
            loginResponseDTO.setIsVerified(user.getIsVerified());

            if (!ObjectUtils.isEmpty(data.getAppVersion())) {
                AppVersion appVersion = new AppVersion();
                appVersion.setCustomerId(user.getId());
                appVersion.setAppVersion(data.getAppVersion());
                appVersion.setPlatformId(data.getPlatformId());
                appVersion.setInstalledAt(data.getInstalledAt());
                appVersion.setCreatedBy(user.getId());
                appVersionRepository.save(appVersion);
            }
            return GeosphereServiceUtility.getBaseResponse(loginResponseDTO);
        }catch ( BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            throw e;
        } catch (ValidationFailureException e) {
            log.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Something went wrong: {}", e.getMessage());
            throw e;
        }
    }
}





