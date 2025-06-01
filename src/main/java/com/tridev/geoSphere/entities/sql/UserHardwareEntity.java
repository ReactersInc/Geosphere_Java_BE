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
@Table(name = "user_hardware")
public class UserHardwareEntity extends BaseEntity {
    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_id", nullable = false, foreignKey = @ForeignKey(name = "fk_hardware"))
    private HardwareEntity hardware;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"))
    private UserEntity user;

    @Column(name = "status_id", nullable = false)
    private Integer statusId = 1;

    @Column(name = "battery")
    private Integer battery;
}
