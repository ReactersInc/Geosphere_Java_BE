package com.tridev.geoSphere.dto.Profile;

import com.tridev.geoSphere.dto.User.UserGeofenceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean publicProfile;
    private Boolean isVerified;
    private String photoUrl;
    private LocalDateTime createdAt;
    private UserGeofenceDTO userGeofenceDetails;

}
