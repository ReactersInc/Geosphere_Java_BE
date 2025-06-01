package com.tridev.geoSphere.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String photo;
    private Boolean publicProfile;
    private String phoneNumber;
    private  String role;
}
