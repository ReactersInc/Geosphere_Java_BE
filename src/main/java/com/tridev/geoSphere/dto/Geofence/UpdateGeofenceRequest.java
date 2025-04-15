package com.tridev.geoSphere.dto.Geofence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateGeofenceRequest {
    private Long geofenceId;
    private String name;
    private String description;
    private List<Coordinates> coordinates;

    private Boolean enableNotifications;
    @JsonProperty("colors")
    private List<String> colors;


}



