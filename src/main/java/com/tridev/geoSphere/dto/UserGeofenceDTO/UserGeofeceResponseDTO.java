package com.tridev.geoSphere.dto.UserGeofenceDTO;

import com.tridev.geoSphere.entities.mongo.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeofeceResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private String photo;

    private boolean locationExists;
    private GeoPoint currentLocation;
    private LocalDateTime lastLocationUpdateTime;
    private Double speed;
    private Double heading;

    private boolean isCurrentlyInside;



}
