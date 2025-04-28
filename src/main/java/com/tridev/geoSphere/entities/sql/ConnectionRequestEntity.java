package com.tridev.geoSphere.entities.sql;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ConnectionRequest")
public class ConnectionRequestEntity extends BaseEntity {

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "RequesterUserId")
    private Long requesterUserId;

    @Column(name = "TargetUserId")
    private Long targetUserId;

    @Column(name = "Status", nullable = false)
    private Integer status;



}
