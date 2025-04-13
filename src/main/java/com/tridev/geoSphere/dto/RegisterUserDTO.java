package com.tridev.geoSphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {


    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String photo;
//    private LocalDate dateOfBirth;

}

