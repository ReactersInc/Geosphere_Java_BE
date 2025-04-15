package com.tridev.geoSphere.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "CreatedBy", updatable = false)
    private Long createdBy;

    @LastModifiedDate
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "UpdatedBy")
    private Long updatedBy;
}
