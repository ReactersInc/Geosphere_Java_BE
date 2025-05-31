package com.tridev.geoSphere.dto.GeofenceRequest;

import com.tridev.geoSphere.dto.Geofence.GeofenceResponseDTO;
import com.tridev.geoSphere.dto.User.UserResponseDTO;
import com.tridev.geoSphere.enums.InvitationStatus;
import com.tridev.geoSphere.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceRequestResponseDTO {
    private Long id;
    private GeofenceResponseDTO geofenceResponse;
    private UserResponseDTO creator;
    private Integer status;
    private ResponseStatus responseStatus;

    // constructors, getters, setters
}
