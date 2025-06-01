package com.tridev.geoSphere.entities.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_locations")
public class UserLocation {
    @Id
    private String id;

    @Indexed
    private Long userId;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoPoint location;

    @Indexed
    private LocalDateTime timestamp;

    private Double speed; // in m/s

    private Double heading; // direction in degrees

    private String deviceInfo;

    private  boolean isInside;
}
