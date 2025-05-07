package com.tridev.geoSphere.services;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void broadcastUserLocation(UserLocation location) {
        // Send to specific topic for this user
        messagingTemplate.convertAndSend(
                "/topic/user/" + location.getUserId() + "/location",
                location
        );
    }

    public void broadcastGeofenceUpdate(Long geofenceId, Object geofenceData) {
        // Send updates to all subscribers of this geofence
        messagingTemplate.convertAndSend(
                "/topic/geofence/" + geofenceId,
                geofenceData
        );
    }
}
