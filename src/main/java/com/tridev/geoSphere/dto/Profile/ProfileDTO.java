package com.tridev.geoSphere.dto.Profile;

import com.tridev.geoSphere.dto.User.UserGeofenceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private String email;
    private String firstName;
    private String lastName;
    private UserGeofenceDTO userGeofenceDetails;

}
