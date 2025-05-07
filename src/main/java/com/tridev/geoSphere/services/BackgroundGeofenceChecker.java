package com.tridev.geoSphere.services;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
import com.tridev.geoSphere.repositories.sql.UserGeofenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class BackgroundGeofenceChecker {
    private final UserGeofenceRepository userGeofenceRepository;
    private final UserLocationRepository userLocationRepository;
    private final LocationTrackingService locationTrackingService;

    // Run every 5 minutes
//    @Scheduled(fixedRate = 300000)
    public void checkGeofenceStatus() throws BadRequestException {
        log.info("Running background geofence check");

        // Get all user-geofence connections
        List<UserGeofenceEntity> allUserGeofences = userGeofenceRepository.findAll();

        for (UserGeofenceEntity userGeofence : allUserGeofences) {
            // Get the latest location for each user
            Optional<UserLocation> latestLocation =
                    userLocationRepository.findTopByUserIdOrderByTimestampDesc(userGeofence.getUserId());

            if (latestLocation.isPresent()) {
                UserLocation location = latestLocation.get();

                // Check if user is still within geofence boundaries
                locationTrackingService.checkGeofenceStatus(
                        userGeofence.getUserId(),
                        location.getLocation().getLatitude(),
                        location.getLocation().getLongitude()
                );
            }
        }

        log.info("Background geofence check completed");
    }
}