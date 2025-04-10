package com.tridev.geoSphere.DTO;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
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

