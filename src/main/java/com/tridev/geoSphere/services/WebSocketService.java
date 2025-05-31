package com.tridev.geoSphere.services;

import com.tridev.geoSphere.entities.mongo.UserLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void broadcastUserLocation(UserLocation location) {
        try {
            if (location == null || location.getUserId() == null) {
                log.error("Cannot broadcast null location or location with null userId");
                return;
            }

            // Send to specific topic for this user
            String destination = "/topic/user/" + location.getUserId() + "/location";
            log.debug("Broadcasting location to {}", destination);

            messagingTemplate.convertAndSend(destination, location);
        } catch (Exception e) {
            log.error("Error broadcasting user location: {}", e.getMessage(), e);
        }
    }

    @Async
    public void sendGeofenceNotification(Long userId, String eventType, String geofenceName, Long geofenceId) {
        try {
            if (userId == null) {
                log.error("Cannot send geofence notification to null userId");
                return;
            }

            String destination = "/topic/user/" + userId + "/geofence";
            log.debug("Sending geofence notification to {}", destination);

            GeofenceNotification notification = new GeofenceNotification(
                    eventType,
                    geofenceName,
                    geofenceId
            );

            messagingTemplate.convertAndSend(destination, notification);
        } catch (Exception e) {
            log.error("Error sending geofence notification: {}", e.getMessage(), e);
        }
    }

    @Async
    public void broadcastGeofenceUpdate(Long geofenceId, Object geofenceData) {
        try {
            if (geofenceId == null) {
                log.error("Cannot broadcast update for null geofenceId");
                return;
            }

            // Send updates to all subscribers of this geofence
            String destination = "/topic/geofence/" + geofenceId;
            log.debug("Broadcasting geofence update to {}", destination);

            messagingTemplate.convertAndSend(destination, geofenceData);
        } catch (Exception e) {
            log.error("Error broadcasting geofence update: {}", e.getMessage(), e);
        }
    }

    // Inner class for geofence notifications
    private static class GeofenceNotification {
        private final String type;
        private final String geofenceName;
        private final Long geofenceId;
        private final long timestamp;

        public GeofenceNotification(String type, String geofenceName, Long geofenceId) {
            this.type = type;
            this.geofenceName = geofenceName;
            this.geofenceId = geofenceId;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getType() { return type; }
        public String getGeofenceName() { return geofenceName; }
        public Long getGeofenceId() { return geofenceId; }
        public long getTimestamp() { return timestamp; }
    }
}
