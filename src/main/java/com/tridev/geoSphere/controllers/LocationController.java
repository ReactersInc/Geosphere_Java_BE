package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.location.LocationUpdateRequest;
import com.tridev.geoSphere.entities.mongo.UserLocation;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.LocationTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/geofence/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationTrackingService locationTrackingService;

    @PostMapping("/update")
    public BaseResponse updateLocation (

            @RequestBody LocationUpdateRequest request)  throws  Exception{


        return  locationTrackingService.updateUserLocation(request);
    }

    @GetMapping("/history")
    public BaseResponse getLocationHistory(@RequestParam(defaultValue = "10") int limit) throws Exception {

        return locationTrackingService.getUserLocationHistory(limit);
    }

    @GetMapping("/user/{userId}/current")
    public BaseResponse getUserCurrentLocation(
            @PathVariable Long userId) throws Exception{
       return locationTrackingService.getCurrentUserLocation();
    }

    @GetMapping("/geofence/{geofenceId}/users")
    public ResponseEntity<List<UserLocation>> getUsersInGeofence(
            @PathVariable Long geofenceId) {
        // This would be implemented by joining user locations with UserGeofence table
        // to get current locations of all users in a specific geofence

        // Not fully implemented in this example
        return ResponseEntity.ok(List.of());
    }
}
