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
@Table(name = "users")
public class RegisterUserEntity {

    @Id
    @Nonnull
    @Column(name = "user_id")
    private  String userId;
    @Column(name = "email")
    private String email;
    @Column(name = "u_pass")
    private String password;
    @Column(name = "f_name")
    private String firstName;
    @Column(name = "l_name")
    private String lastName;
//    @Column(name = "DateOfBirth")
//    private LocalDate dateOfBirth;
    @Column(name = "created_at")
    private LocalDateTime registeredAt;
//    @Column(name = "Role")
//    private String role;
    @Column(name = "photo")
    private String photo;

}
