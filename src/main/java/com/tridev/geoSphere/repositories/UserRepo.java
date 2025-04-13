package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Integer > {

    Optional<UserEntity> findByEmail(String email);
//    Optional<RegisterUserEntity> findTopByOrderByUserIdDesc();
}
