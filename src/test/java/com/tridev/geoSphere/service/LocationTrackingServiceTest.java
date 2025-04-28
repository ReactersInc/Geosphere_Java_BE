//package com.tridev.geoSphere.service;
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tridev.geoSphere.entities.mongo.GeoPoint;
//import com.tridev.geoSphere.entities.mongo.UserLocation;
//import com.tridev.geoSphere.entities.sql.GeofenceEntity;
//import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
//import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
//import com.tridev.geoSphere.repositories.sql.GeofenceRepository;
//import com.tridev.geoSphere.repositories.sql.UserGeofenceRepository;
//import com.tridev.geoSphere.services.LocationTrackingService;
//import com.tridev.geoSphere.services.NotificationService;
//import com.tridev.geoSphere.services.WebSocketService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.locationtech.jts.geom.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LocationTrackingServiceTest {
//
//    @Mock
//    private UserLocationRepository userLocationRepository;
//
//    @Mock
//    private GeofenceRepository geofenceRepository;
//
//    @Mock
//    private UserGeofenceRepository userGeofenceRepository;
//
//    @Mock
//    private NotificationService notificationService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private WebSocketService webSocketService;
//
//    @InjectMocks
//    private LocationTrackingService locationTrackingService;
//
//    private GeometryFactory geometryFactory;
//
//    @BeforeEach
//    void setUp() {
//        geometryFactory = new GeometryFactory();
//    }
//
//    @Test
//    void updateUserLocation_shouldSaveWhenSignificantMovement() {
//        // Arrange
//        Long userId = 1L;
//        double latitude = 40.7128;
//        double longitude = -74.0060;
//
//        when(userLocationRepository.findLatestByUserId(userId)).thenReturn(Optional.empty());
//        when(userLocationRepository.save(any(UserLocation.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        UserLocation result = locationTrackingService.updateUserLocation(
//                userId, latitude, longitude, 0.0, 0.0, "test-device");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(userId, result.getUserId());
//        assertEquals(latitude, result.getLocation().getLatitude(), 0.0001);
//        assertEquals(longitude, result.getLocation().getLongitude(), 0.0001);
//        verify(userLocationRepository, times(1)).save(any(UserLocation.class));
//    }
//
//    @Test
//    void updateUserLocation_shouldNotSaveWhenInsignificantMovement() {
//        // Arrange
//        Long userId = 1L;
//        double latitude1 = 40.7128;
//        double longitude1 = -74.0060;
//        double latitude2 = 40.712805; // ~0.5 meters difference
//        double longitude2 = -74.006005;
//
//        UserLocation previousLocation = new UserLocation();
//        previousLocation.setUserId(userId);
//        previousLocation.setLocation(new GeoPoint(longitude1, latitude1));
//        previousLocation.setTimestamp(LocalDateTime.now().minusMinutes(5));
//
//        when(userLocationRepository.findLatestByUserId(userId)).thenReturn(Optional.of(previousLocation));
//
//        // Act
//        UserLocation result = locationTrackingService.updateUserLocation(
//                userId, latitude2, longitude2, 0.0, 0.0, "test-device");
//
//        // Assert
//        assertEquals(previousLocation, result);
//        verify(userLocationRepository, never()).save(any(UserLocation.class));
//    }
//
//    @Test
//    void checkGeofenceStatus_shouldDetectExit() {
//        // Arrange
//        Long userId = 1L;
//        Long geofenceId = 1L;
//        double latitude = 40.7128;
//        double longitude = -74.0060;
//
//        UserGeofenceEntity userGeofence = new UserGeofenceEntity();
//        userGeofence.setUserId(userId);
//        userGeofence.setGeofenceId(geofenceId);
//        userGeofence.setIsCurrentlyInside(true);
//
//        GeofenceEntity geofence = new GeofenceEntity();
//        geofence.setId(geofenceId);
//        geofence.setName("Test Geofence");
//        geofence.setCreatedBy(2L);
//        geofence.setCoordinates("{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[0,1],[1,1],[1,0],[0,0]]]}");
//
//        when(userGeofenceRepository.findByUserId(userId)).thenReturn(List.of(userGeofence));
//        when(geofenceRepository.findById(geofenceId)).thenReturn(Optional.of(geofence));
//
//        // Act
//        locationTrackingService.checkGeofenceStatus(userId, latitude, longitude);
//
//        // Assert
//        assertFalse(userGeofence.getIsCurrentlyInside());
//        verify(notificationService, times(1)).sendGeofenceExitNotification(
//                eq(geofence.getCreatedBy()), eq(userId), eq(geofenceId), eq(geofence.getName()));
//    }
//
//    // Add more test cases for other scenarios
//}
