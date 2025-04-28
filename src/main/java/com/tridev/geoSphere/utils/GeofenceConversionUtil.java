package com.tridev.geoSphere.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tridev.geoSphere.dto.Geofence.Coordinates;
import com.tridev.geoSphere.dto.Geofence.GeofenceResponse;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;

import java.util.Collections;
import java.util.List;

public class GeofenceConversionUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Coordinates> parseCoordinates(String coordinatesJson) {
        try {
            return objectMapper.readValue(coordinatesJson, new TypeReference<List<Coordinates>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // fallback
        }
    }

    public static String convertCoordinatesToJson(List<Coordinates> coordinatesList) {
        try {
            return objectMapper.writeValueAsString(coordinatesList);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public static String extractColors(String colorsRaw) {
        return colorsRaw != null ? colorsRaw : "";
    }

    public static GeofenceResponse convertEntityToResponse(GeofenceEntity entity) {
        GeofenceResponse response = new GeofenceResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setEnableNotifications(entity.getEnableNotifications());
        response.setAlertCount(entity.getAlertCount());
        response.setStatus(entity.getStatus());
        response.setCoordinates(parseCoordinates(entity.getCoordinates()));
        response.setColors(extractColors(entity.getColors()));
        return response;
    }
}
