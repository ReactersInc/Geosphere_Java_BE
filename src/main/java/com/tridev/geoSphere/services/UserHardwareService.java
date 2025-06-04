package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.hardware.AssignHardwareRequest;
import com.tridev.geoSphere.dto.hardware.HardwareProjection;
import com.tridev.geoSphere.dto.hardware.UserHardwareResponse;
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
import jakarta.transaction.Transactional;
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

        // Validate userId in request
        if (request.getUserId() == null || request.getHardwareMac() == null) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        // Fetch hardware by MAC address
        Optional<HardwareEntity> hardwareEntityOpt = hardwareRepository.findByMacAddressAndStatusId(
                request.getHardwareMac(), Status.ACTIVE.getValue());

        if (hardwareEntityOpt.isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_NOT_FOUND);
        }

        HardwareEntity hardware = hardwareEntityOpt.get();

        // Validate user
        Optional<UserEntity> userOpt = userRepository.findByIdAndStatus(
                request.getUserId().longValue(), Status.ACTIVE.getValue());

        if (userOpt.isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.USER_NOT_FOUND);
        }

        UserEntity user = userOpt.get();

        // Check if hardware is already assigned to the user
        boolean alreadyAssigned = userHardwareRepository.existsByUser_IdAndHardware_IdAndStatusId(
                user.getId(), hardware.getId(), Status.ACTIVE.getValue());

        if (alreadyAssigned) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_ALREADY_ASSIGNED);
        }
        UserHardwareEntity userHardware = new UserHardwareEntity();
        userHardware.setUser(user);
        userHardware.setHardware(hardware);
        userHardware.setStatusId(Status.ACTIVE.getValue());
        userHardware.setBattery(request.getBattery());
        userHardware.setCreatedAt(LocalDateTime.now());
        userHardware.setUpdatedAt(LocalDateTime.now());
        userHardware.setCreatedBy(requesterUserId);
        userHardware.setUpdatedBy(requesterUserId);

        userHardwareRepository.save(userHardware);
        return GeosphereServiceUtility.getBaseResponse("Hardware assigned to user successfully.");
    }

    @Transactional
    public BaseResponse getHardware() {
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("Fetching hardware for userId: {}, requested by userId: {}", requesterUserId, requesterUserId);

        List<HardwareProjection> hardwareList = userHardwareRepository.findAllHardwareByUserIdAndStatusId(
                requesterUserId, Status.ACTIVE.getValue());

        UserHardwareResponse response = new UserHardwareResponse();
        response.setTotalHardware((long) hardwareList.size());
        response.setHardwareList(hardwareList);

        return GeosphereServiceUtility.getBaseResponse(response);
    }
}
