package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.hardware.HardwareLocationDTO;
import com.tridev.geoSphere.entities.mongo.GeoPoint;
import com.tridev.geoSphere.entities.mongo.UserLocation;
import com.tridev.geoSphere.entities.sql.HardwareEntity;
import com.tridev.geoSphere.entities.sql.UserHardwareEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
import com.tridev.geoSphere.repositories.sql.HardwareRepository;
import com.tridev.geoSphere.repositories.sql.UserHardwareRepository;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
@Slf4j
@Service
public class HardwareLocationService {
    @Autowired
    private LocationTrackingService locationTrackingService;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private HardwareRepository hardwareRepository;

    @Autowired
    private UserHardwareRepository userHardwareRepository;

    @Transactional
    public BaseResponse updateHardwareLocation(HardwareLocationDTO hardwareLocationDTO) throws Exception {
        if (hardwareLocationDTO.getMacAddress() == null || hardwareLocationDTO.getMacAddress().isEmpty()) {
            throw new BadRequestException(CommonValidationConstant.INVALID_REQUEST);
        }
        if (hardwareLocationDTO.getLatitude() == null || hardwareLocationDTO.getLongitude() == null) {
            throw new BadRequestException(CommonValidationConstant.INVALID_LAT_LONG);
        }
        HardwareEntity hardware =  hardwareRepository.findByMacAddressAndStatusId(hardwareLocationDTO.getMacAddress(), Status.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException(CommonValidationConstant.HARDWARE_NOT_FOUND));

        UserHardwareEntity userHardware = userHardwareRepository.findByHardwareAndStatusId(hardware, Status.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException(CommonValidationConstant.INVALID_HARDWARE));
        log.info("Updating location for hardware: {}, user: {}", hardware.getId(), userHardware.getUser().getId());
        Long userId = userHardware.getUser().getId();
        UserLocation newLocation = new UserLocation();
        newLocation.setUserId(userId);
        newLocation.setLocation(new GeoPoint(hardwareLocationDTO.getLongitude(), hardwareLocationDTO.getLatitude()));
        newLocation.setTimestamp(LocalDateTime.now());
        newLocation.setSpeed(hardwareLocationDTO.getSpeed());
        newLocation.setHeading(hardwareLocationDTO.getHeading());
        newLocation.setDeviceInfo(hardwareLocationDTO.getDeviceInfo());
        UserLocation savedLocation = userLocationRepository.save(newLocation);

        Map<Long, Boolean> geofenceStatuses = locationTrackingService.checkGeofenceStatus(userId, hardwareLocationDTO.getLatitude(), hardwareLocationDTO.getLongitude());
        webSocketService.broadcastUserLocationWithStatus(savedLocation, geofenceStatuses);
        log.info("✅ Location updated for hardware: {}, user: {}", hardware.getId(), userId);
        return GeosphereServiceUtility.getBaseResponse(savedLocation);
    }
}
