package com.tridev.geoSphere.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class RegisterUserEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private  Integer Id;
    @Column(name = "Email")
    private String email;
    @Column(name = "Password")
    private String password;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
//    @Column(name = "DateOfBirth")
//    private LocalDate dateOfBirth;
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    @Column(name = "Role")
    private String role;
    @Column(name = "Photo")
    private String photo;

    @Column(name = "Otp")
    private String otp;

    @Column(name = "IsVerified")
    private Boolean isVerified;

    @Column(name = "Status")
    private Integer status;

}
