package com.tridev.geoSphere.entities.sql;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserContacts")
public class UserContactsEntity extends BaseEntity {
    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Nonnull
    @Column(name = "UserId", nullable = false)
    private Long userId;

    @Nonnull
    @Column(name = "ContactUserId", nullable = false)
    private Long contactUserId;

    @Column(name = "Status")
    private Integer status;
}