package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.RegisterUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<RegisterUserEntity,Long > {

    Optional<RegisterUserEntity> findByEmail(String email);
}
