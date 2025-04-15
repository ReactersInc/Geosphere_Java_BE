package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Long > {

    Optional<UserEntity> findByEmailAndStatus(String email, Integer status);


    Optional<UserEntity> findById(Long id);


//    Optional<RegisterUserEntity> findTopByOrderByUserIdDesc();
}
