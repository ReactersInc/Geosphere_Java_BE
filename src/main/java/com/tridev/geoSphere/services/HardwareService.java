package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.hardware.AddHardwareRequest;
import com.tridev.geoSphere.entities.sql.HardwareEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.sql.HardwareRepository;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class HardwareService {
    @Autowired
    private HardwareRepository hardwareRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public BaseResponse addHardware(AddHardwareRequest request) throws Exception {
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("Adding hardware by userId: {}", requesterUserId);

        if (ObjectUtils.isEmpty(request.getModel()) || ObjectUtils.isEmpty(request.getMacAddress())) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }

        if (hardwareRepository.existsByMacAddressAndStatusId(request.getMacAddress(), Status.ACTIVE.getValue())) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_ALREADY_EXISTS);
        }

        HardwareEntity hardware = new HardwareEntity();
        hardware.setModel(request.getModel());
        hardware.setManufacturer(request.getManufacturer());
        hardware.setMacAddress(request.getMacAddress());
        hardware.setStatusId(Status.ACTIVE.getValue());

        hardwareRepository.save(hardware);
        return GeosphereServiceUtility.getBaseResponse("Hardware added successfully.");
    }

    public BaseResponse softDeleteHardware(String id) throws Exception {
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("Soft deleting hardware by userId: {}, hardwareId: {}", requesterUserId, id);

        Long hardwareId;
        try {
            hardwareId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException(CommonValidationConstant.INVALID_HARDWARE_ID);
        }

        HardwareEntity hardware = hardwareRepository.findByIdAndStatusId(hardwareId, Status.ACTIVE.getValue());
        if (hardware == null) {
            throw new BadRequestException(CommonValidationConstant.HARDWARE_NOT_FOUND);
        }

        hardware.setStatusId(Status.DELETED.getValue());
        hardwareRepository.save(hardware);

        return GeosphereServiceUtility.getBaseResponse("Hardware soft-deleted successfully.");
    }
}
