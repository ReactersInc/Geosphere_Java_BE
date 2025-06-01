package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.hardware.AssignHardwareRequest;
import com.tridev.geoSphere.entities.sql.HardwareEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.entities.sql.UserHardwareEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.sql.HardwareRepository;
import com.tridev.geoSphere.repositories.sql.UserHardwareRepository;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserHardwareService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private HardwareRepository hardwareRepository;

    @Autowired
    private UserHardwareRepository userHardwareRepository;

    public BaseResponse assignHardwareToUser(AssignHardwareRequest request) throws Exception {
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("Assigning hardware by userId: {}", requesterUserId);

        if (request.getUserId() == null || request.getHardwareId() == null) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        Optional<UserEntity> user = userRepository.findByIdAndStatus(request.getUserId().longValue(), Status.ACTIVE.getValue());
        if (user.isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
        }
        UserEntity userEntity = user.get();
        HardwareEntity hardware = hardwareRepository.findByIdAndStatusId(Long.valueOf(request.getHardwareId()), Status.ACTIVE.getValue());
        if (hardware == null) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_NOT_FOUND);
        }

        Long hardwareId = Long.valueOf(request.getHardwareId());
        boolean alreadyAssigned = userHardwareRepository.existsByUser_IdAndHardware_IdAndStatusId(
                request.getUserId().longValue(), hardwareId, Status.ACTIVE.getValue());

        if (alreadyAssigned) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_ALREADY_ASSIGNED);
        }
        UserHardwareEntity userHardware = new UserHardwareEntity();
        userHardware.setUser(userEntity);
        userHardware.setHardware(hardware);
        userHardware.setStatusId(Status.ACTIVE.getValue());
        userHardware.setBattery(request.getBattery());
        userHardware.setCreatedAt(LocalDateTime.now());
        userHardware.setUpdatedAt(LocalDateTime.now());
        userHardware.setUpdatedBy(userEntity.getId());
        userHardware.setCreatedBy(userEntity.getId());
        userHardwareRepository.save(userHardware);
        return GeosphereServiceUtility.getBaseResponse("Hardware assigned to user successfully.");
    }

    public BaseResponse getHardware() {
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("Fetching hardware for userId: {}, requested by userId: {}", requesterUserId, requesterUserId);

        List<UserHardwareEntity> userHardwareList = userHardwareRepository.findAllByUser_IdAndStatusId(
                12L, Status.ACTIVE.getValue());

        return GeosphereServiceUtility.getBaseResponse(userHardwareList);
    }
}
