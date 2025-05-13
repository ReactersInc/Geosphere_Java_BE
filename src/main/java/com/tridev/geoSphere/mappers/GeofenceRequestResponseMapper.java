package com.tridev.geoSphere.mappers;

import com.tridev.geoSphere.dto.Geofence.GeofenceResponseDTO;
import com.tridev.geoSphere.dto.GeofenceRequest.GeofenceRequestResponseDTO;
import com.tridev.geoSphere.dto.User.UserResponseDTO;
import com.tridev.geoSphere.entities.sql.GeofenceEntity;
import com.tridev.geoSphere.entities.sql.GeofenceRequestEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class GeofenceRequestResponseMapper {

    // In your mapper class
    public GeofenceRequestResponseDTO toResponseDTO(GeofenceRequestEntity request,
                                                    GeofenceEntity geofence,
                                                    UserEntity creator) {
        GeofenceRequestResponseDTO dto = new GeofenceRequestResponseDTO();
        dto.setId(request.getId());

        // Map geofence
        GeofenceResponseDTO geofenceDTO = new GeofenceResponseDTO();
        geofenceDTO.setId(geofence.getId());
        geofenceDTO.setName(geofence.getName());
        geofenceDTO.setDescription(geofence.getDescription());
        geofenceDTO.setCreatedAt(geofence.getCreatedAt());
        // Map other geofence properties
        dto.setGeofenceResponse(geofenceDTO);

        // Map creator
        UserResponseDTO creatorDTO = new UserResponseDTO();
        creatorDTO.setId(creator.getId());
        creatorDTO.setFirstName(creator.getFirstName());
        creatorDTO.setLastName(creator.getLastName());
        creatorDTO.setEmail(creator.getEmail());
        creatorDTO.setPhoto(creator.getPhoto());
        dto.setCreator(creatorDTO);

        // Map other properties
        dto.setStatus(request.getStatus());



        return dto;
    }
}
