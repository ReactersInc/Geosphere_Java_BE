package com.tridev.geoSphere.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.location.LocationUpdateRequest;
import com.tridev.geoSphere.entities.mongo.UserLocation;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;
import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
import com.tridev.geoSphere.entities.mongo.GeoPoint;

import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
import com.tridev.geoSphere.repositories.sql.GeofenceRepository;
import com.tridev.geoSphere.repositories.sql.UserGeofenceRepository;

import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationTrackingService {
    private final UserLocationRepository userLocationRepository;
    private final GeofenceRepository geofenceRepository;
    private final UserGeofenceRepository userGeofenceRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GeoFenceLocationService geoFenceLocationService;

    private final WebSocketService webSocketService;


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public BaseResponse updateUserLocation(LocationUpdateRequest request) throws BadRequestException {
        Long userId = jwtUtil.getUserIdFromToken();
        // Get previous location to check for significant movement
        //Optional<UserLocation> previousLocationOpt = userLocationRepository.findTopByUserIdOrderByTimestampDesc(userId);


        // Create new location entry
        UserLocation newLocation = new UserLocation();
        newLocation.setUserId(userId);
        newLocation.setLocation(new GeoPoint(request.getLongitude(), request.getLatitude()));
        newLocation.setTimestamp(LocalDateTime.now());
        newLocation.setSpeed(request.getSpeed());
        newLocation.setHeading(request.getHeading());
        newLocation.setDeviceInfo(request.getDeviceInfo());


        // Check if movement is significant (>5 meters)
//        boolean isSignificantMovement = true;
//        if (previousLocationOpt.isPresent()) {
//            UserLocation prevLocation = previousLocationOpt.get();
//            double distance = calculateDistance(
//                    prevLocation.getLocation().getLatitude(),
//                    prevLocation.getLocation().getLongitude(),
//                    request.getLatitude(), request.getLongitude()
//            );
//
//            isSignificantMovement = distance >= 5.0;
//        }






            UserLocation savedLocation = userLocationRepository.save(newLocation);


            // Check geofence boundaries
        Map<Long, Boolean> geofenceStatuses = checkGeofenceStatus(userId, request.getLatitude(), request.getLongitude());

            webSocketService.broadcastUserLocationWithStatus(savedLocation, geofenceStatuses);

            return GeosphereServiceUtility.getBaseResponse(savedLocation);

    }

    public Map<Long, Boolean> checkGeofenceStatus(Long userId, double latitude, double longitude) throws BadRequestException {
        List<UserGeofenceEntity> userGeofences = userGeofenceRepository.findByUserId(userId);
        Point userPoint = geometryFactory.createPoint(new Coordinate(latitude, longitude));

        Map<Long, Boolean> geofenceStatusMap = new HashMap<>();

        userGeofences.parallelStream().forEach(userGeofence -> {
            GeofenceEntity geofence;
            try {
                geofence = geofenceRepository.findById(userGeofence.getGeofenceId())
                        .orElseThrow(() -> new BadRequestException(CommonValidationConstant.GEOFENCE_NOT_FOUND));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }

            boolean isInside = isPointInGeofence(userPoint, geofence.getCoordinates());
            geofenceStatusMap.put(geofence.getId(), isInside);

            if (userGeofence.getIsCurrentlyInside() && !isInside) {
                userGeofence.setIsCurrentlyInside(false);
                userGeofence.setLastExitedAt(System.currentTimeMillis());
                userGeofenceRepository.save(userGeofence);
                notificationService.sendGeofenceExitNotification(
                        geofence.getCreatedBy(), userId, geofence.getId(), geofence.getName());
            } else if (!userGeofence.getIsCurrentlyInside() && isInside) {
                userGeofence.setIsCurrentlyInside(true);
                userGeofence.setLastEnteredAt(System.currentTimeMillis());
                userGeofenceRepository.save(userGeofence);
                notificationService.sendGeofenceEntryNotification(
                        geofence.getCreatedBy(), userId, geofence.getId(), geofence.getName());
            }
        });

        return geofenceStatusMap;
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
    private Polygon createPolygonFromCoordinates(List<List<Double>> coordinates) {
        Coordinate[] coords = new Coordinate[coordinates.size() + 1];

        for (int i = 0; i < coordinates.size(); i++) {
            List<Double> point = coordinates.get(i);
            coords[i] = new Coordinate(point.get(0), point.get(1));
        }

        // Close the polygon by adding first point at the end
        coords[coordinates.size()] = new Coordinate(coordinates.get(0).get(0), coordinates.get(0).get(1));

        LinearRing ring = geometryFactory.createLinearRing(coords);
        return geometryFactory.createPolygon(ring);
    }

    // Haversine formula for calculating distance between two points on Earth
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in meters
    }

    public BaseResponse getUserLocationHistory( int limit) throws Exception {
        Long userId = jwtUtil.getUserIdFromToken();
        List<UserLocation> list = userLocationRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream().limit(limit).toList();
        return GeosphereServiceUtility.getBaseResponse(list);
    }

    public BaseResponse getCurrentUserLocation() throws Exception{
        Long userId = jwtUtil.getUserIdFromToken();
        Optional<UserLocation> latestByUserId = userLocationRepository.findTopByUserIdOrderByTimestampDesc(userId);
        return  GeosphereServiceUtility.getBaseResponse(latestByUserId.orElse(null));
    }
}
