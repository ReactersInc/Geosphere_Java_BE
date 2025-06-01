package com.tridev.geoSphere.dto.location;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationWithGeofenceStatus {
    private UserLocation location;
    private Map<Long, Boolean> geofenceStatusMap; // geofenceId -> isInside
}