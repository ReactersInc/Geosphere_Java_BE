package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.Geofence.GeofenceRequest;
import com.tridev.geoSphere.dto.Geofence.UpdateGeofenceRequest;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.GeofenceService;
import com.tridev.geoSphere.services.UserServiceDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geofence")
public class GeofenceController {
    @Autowired
    private UserServiceDetailsImpl userServiceDetails;
    private final GeofenceService geofenceService;

    @Autowired
    public GeofenceController(GeofenceService geofenceService) {
        this.geofenceService = geofenceService;
    }

    @PostMapping
    public BaseResponse createGeofence(@RequestBody GeofenceRequest request) throws Exception  {
        return geofenceService.createGeofence(request);
    }

    @PutMapping()
    public BaseResponse updateGeofence( @RequestBody UpdateGeofenceRequest updateGeofenceRequest) throws Exception  {
        return geofenceService.updateGeofence(updateGeofenceRequest);
    }
//
//    @DeleteMapping("/{id}")
//    public BaseResponse deleteGeofence(@PathVariable Long id) throws Exception  {
//        return geofenceService.deleteGeofence(id);
//    }
//
//    @GetMapping("/{id}")
//    public BaseResponse getGeofenceById(@PathVariable Long id) throws Exception {
//        return geofenceService.getGeofenceById(id);
//    }
//
//    @GetMapping
//    public BaseResponse getAllGeofences(
//            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
//            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
//            @RequestParam(name = "sortOrder", defaultValue = "DESC") String sortOrder) throws Exception  {
//
//        return geofenceService.getAllGeofences(pageNo, pageSize, sortOrder);
//    }
}