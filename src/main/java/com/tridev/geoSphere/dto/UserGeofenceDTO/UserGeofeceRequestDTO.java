package com.tridev.geoSphere.dto.UserGeofenceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeofeceRequestDTO {
    private String email;
    private Long geofenceId;


}
