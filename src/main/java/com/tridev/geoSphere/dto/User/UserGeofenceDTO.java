package com.tridev.geoSphere.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeofenceDTO {
    private Integer totalGeofence;
    private Integer totalGeofenceInUse;
    private Integer totalGeofenceNotInUse;
    private Integer totalUsersInGeofence;
    private Integer totalConnections;
    private Integer pendingGeofenceRequests;
    private Integer pendingConnectionRequests;
    private List<GeofenceDetailsDTO> inWitchGeofence;
}
