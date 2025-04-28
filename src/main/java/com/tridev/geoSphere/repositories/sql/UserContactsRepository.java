package com.tridev.geoSphere.repositories.sql;

import com.tridev.geoSphere.entities.sql.UserContactsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContactsRepository extends JpaRepository<UserContactsEntity, Long> {
    UserContactsEntity findByUserIdAndContactUserId(Long userId, Long contactUserId);

   Page<UserContactsEntity> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);



    boolean existsByUserIdAndContactUserId(Long userId, Long contactUserId);
}
