package com.tridev.geoSphere.entities.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint {
    private String type = "Point";
    private double[] coordinates; // [longitude, latitude]

    public GeoPoint(double longitude, double latitude) {
        this.coordinates = new double[]{longitude, latitude};
    }

    public double getLongitude() {
        return coordinates[0];
    }

    public double getLatitude() {
        return coordinates[1];
    }
}