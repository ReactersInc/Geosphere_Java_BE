package com.tridev.geoSphere.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;
import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
import com.tridev.geoSphere.repositories.sql.GeofenceRepository;
import com.tridev.geoSphere.repositories.sql.UserGeofenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GeoFenceLocationService {

    @Autowired
    private UserLocationRepository userLocationRepository;
    @Autowired
    private  GeofenceRepository geofenceRepository;
    @Autowired
    private  UserGeofenceRepository userGeofenceRepository;
    @Autowired
    private  NotificationService notificationService;
    @Autowired
    private  ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Async
    public void checkGeofenceStatus(Long userId, double latitude, double longitude) throws BadRequestException {
        // Get all geofences this user is part of
        List<UserGeofenceEntity> userGeofences = userGeofenceRepository.findByUserId(userId);

        Point userPoint = geometryFactory.createPoint(new Coordinate(latitude,longitude ));

        for (UserGeofenceEntity userGeofence : userGeofences) {
            GeofenceEntity geofence = geofenceRepository.findById(userGeofence.getGeofenceId())
                    .orElseThrow(() -> new BadRequestException(CommonValidationConstant.GEOFENCE_NOT_FOUND));

            boolean isInside = isPointInGeofence(userPoint, geofence.getCoordinates());

            // If user was inside but now outside, update status and notify
            if (userGeofence.getIsCurrentlyInside() && !isInside) {
                userGeofence.setIsCurrentlyInside(false);
                userGeofence.setLastExitedAt(System.currentTimeMillis());
                userGeofenceRepository.save(userGeofence);

                // Send notification to geofence creator
                notificationService.sendGeofenceExitNotification(
                        geofence.getCreatedBy(), userId, geofence.getId(), geofence.getName());
            }
            // If user was outside but now inside, update status
            else if (!userGeofence.getIsCurrentlyInside() && isInside) {
                userGeofence.setIsCurrentlyInside(true);
                userGeofence.setLastEnteredAt(System.currentTimeMillis());
                userGeofenceRepository.save(userGeofence);

                // Optionally notify about re-entry
                notificationService.sendGeofenceEntryNotification(
                        geofence.getCreatedBy(), userId, geofence.getId(), geofence.getName());
            }
        }
    }

    private boolean isPointInGeofence(Point point, String geofenceCoordinates) {
        try {
            // Parse your custom format: [{"lat":..., "lang":...}]
            List<Map<String, Double>> coordList = objectMapper.readValue(
                    geofenceCoordinates,
                    new TypeReference<List<Map<String, Double>>>() {}
            );

            // Convert to Coordinate array (latitude, longitude) to match userPoint
            Coordinate[] coordinates = new Coordinate[coordList.size() + 1]; // +1 to close polygon
            for (int i = 0; i < coordList.size(); i++) {
                Map<String, Double> coord = coordList.get(i);
                coordinates[i] = new Coordinate(coord.get("lat"), coord.get("lang")); // lat, lng
            }
            coordinates[coordList.size()] = coordinates[0].copy(); // Close the polygon

            Polygon polygon = geometryFactory.createPolygon(coordinates);
            return polygon.contains(point);
        } catch (Exception e) {
            log.error("Error checking if point is in geofence", e);
            return false;
        }
    }

}
