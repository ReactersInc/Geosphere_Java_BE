package com.tridev.geoSphere.repositories.mongo;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserLocationRepository extends MongoRepository<UserLocation, String> {
    List<UserLocation> findByUserId(Long userId);

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'timestamp': -1 }")
    Optional<UserLocation> findLatestByUserId(Long userId);

    List<UserLocation> findByUserIdOrderByTimestampDesc(Long userId);

    Optional<UserLocation> findTopByUserIdOrderByTimestampDesc(Long userId);
}
