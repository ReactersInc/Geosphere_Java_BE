package com.tridev.geoSphere.repositories.mongo;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserLocationRepository extends MongoRepository<UserLocation, String> {
    List<UserLocation> findByUserId(Long userId);

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'timestamp': -1 }")
    Optional<UserLocation> findLatestByUserId(Long userId);

    List<UserLocation> findByUserIdOrderByTimestampDesc(Long userId);

    Optional<UserLocation> findTopByUserIdOrderByTimestampDesc(Long userId);

    @Query(value = "{ 'userId': { $in: ?0 } }", sort = "{ 'timestamp': -1 }")
    List<UserLocation> findLatestByUserIds(List<Long> userIds);

    // This uses MongoDB's aggregation to get only the latest location per user
    @Aggregation(pipeline = {
            "{ $match: { 'userId': { $in: ?0 } } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $group: { '_id': '$userId', 'latestLocation': { $first: '$$ROOT' } } }",
            "{ $replaceRoot: { 'newRoot': '$latestLocation' } }"
    })
    List<UserLocation> findLatestLocationsForUsers(List<Long> userIds);
}
