package com.tridev.geoSphere.repositories;

import com.tridev.geoSphere.entities.UserEntity;
import com.tridev.geoSphere.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Long > {

    Optional<UserEntity> findByEmailAndStatus(String email, Integer status);


    Optional<UserEntity> findById(Long id);

    Page<UserEntity> findByPublicProfile(Boolean publicProfile, Pageable pageable);



//    Optional<RegisterUserEntity> findTopByOrderByUserIdDesc();

    Page<UserEntity> findByPublicProfileTrueAndIdNot(Long id, Pageable pageable);


    List<UserEntity> findAllById(Iterable<Long> ids);

    Optional<UserEntity> findByIdAndStatus(Long userId, Integer value);
}
