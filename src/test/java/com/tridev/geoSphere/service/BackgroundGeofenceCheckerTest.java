//
//
//package com.tridev.geoSphere.service;
//
//import com.tridev.geoSphere.entities.mongo.GeoPoint;
//import com.tridev.geoSphere.entities.mongo.UserLocation;
//import com.tridev.geoSphere.entities.sql.UserGeofenceEntity;
//import com.tridev.geoSphere.repositories.mongo.UserLocationRepository;
//import com.tridev.geoSphere.repositories.sql.UserGeofenceRepository;
//import com.tridev.geoSphere.services.BackgroundGeofenceChecker;
//import com.tridev.geoSphere.services.LocationTrackingService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BackgroundGeofenceCheckerTest {
//
//    @Mock
//    private UserGeofenceRepository userGeofenceRepository;
//
//    @Mock
//    private UserLocationRepository userLocationRepository;
//
//    @Mock
//    private LocationTrackingService locationTrackingService;
//
//    @InjectMocks
//    private BackgroundGeofenceChecker backgroundGeofenceChecker;
//
//    @Test
//    void checkGeofenceStatus_shouldProcessAllUserGeofences() {
//        // Arrange
//        Long userId = 1L;
//        Long geofenceId = 1L;
//
//        UserGeofenceEntity userGeofence = new UserGeofenceEntity();
//        userGeofence.setUserId(userId);
//        userGeofence.setGeofenceId(geofenceId);
//
//        UserLocation location = new UserLocation();
//        location.setUserId(userId);
//        location.setLocation(new GeoPoint(-74.0060, 40.7128));
//
//        when(userGeofenceRepository.findAll()).thenReturn(List.of(userGeofence));
//        when(userLocationRepository.findLatestByUserId(userId)).thenReturn(Optional.of(location));
//
//        // Act
//        backgroundGeofenceChecker.checkGeofenceStatus();
//
//        // Assert
//        verify(locationTrackingService, times(1)).checkGeofenceStatus(
//                eq(userId),
//                eq(location.getLocation().getLatitude()),
//                eq(location.getLocation().getLongitude())
//        );
//    }
//
//    @Test
//    void checkGeofenceStatus_shouldSkipWhenNoLocation() {
//        // Arrange
//        Long userId = 1L;
//        Long geofenceId = 1L;
//
//        UserGeofenceEntity userGeofence = new UserGeofenceEntity();
//        userGeofence.setUserId(userId);
//        userGeofence.setGeofenceId(geofenceId);
//
//        when(userGeofenceRepository.findAll()).thenReturn(List.of(userGeofence));
//        when(userLocationRepository.findLatestByUserId(userId)).thenReturn(Optional.empty());
//
//        // Act
//        backgroundGeofenceChecker.checkGeofenceStatus();
//
//        // Assert
//        verify(locationTrackingService, never()).checkGeofenceStatus(any(), anyDouble(), anyDouble());
//    }
//}
