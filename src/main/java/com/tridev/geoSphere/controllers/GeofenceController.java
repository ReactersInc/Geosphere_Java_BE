package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.Geofence.GeofenceRequest;
import com.tridev.geoSphere.dto.Geofence.UpdateGeofenceRequest;
import com.tridev.geoSphere.dto.UserGeofenceDTO.UserGeofeceRequestDTO;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.GeofenceService;
import com.tridev.geoSphere.services.UserGeofenceService;
import com.tridev.geoSphere.utils.UserServiceDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geofence")
@Slf4j
public class GeofenceController {
    @Autowired
    private UserServiceDetailsImpl userServiceDetails;

    @Autowired
    private  GeofenceService geofenceService;

    @Autowired
    private UserGeofenceService userGeofenceService;





    @PostMapping
    public BaseResponse createGeofence(@RequestBody GeofenceRequest request) throws Exception  {

        return geofenceService.createGeofence(request);
    }

    @PutMapping()
    public BaseResponse updateGeofence( @RequestBody UpdateGeofenceRequest updateGeofenceRequest) throws Exception  {
        return geofenceService.updateGeofence(updateGeofenceRequest);
    }
//
    @DeleteMapping("/{id}")
    public BaseResponse deleteGeofence(@PathVariable Long id) throws Exception  {
        return geofenceService.deleteGeofence(id);
    }
//
    @GetMapping()
    public BaseResponse getGeofenceByUserId(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) throws Exception {
        return geofenceService.getGeofencesByUser(page, size);
    }
//
//    @GetMapping
//    public BaseResponse getAllGeofences(
//            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
//            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
//            @RequestParam(name = "sortOrder", defaultValue = "DESC") String sortOrder) throws Exception  {
//
//        return geofenceService.getAllGeofences(pageNo, pageSize, sortOrder);
//    }


    @GetMapping("/{id}")
    public BaseResponse getGeofenceById(@PathVariable Long id) throws Exception{

        return geofenceService.getGeofenceById(id);

    }

    @PatchMapping("/{id}/notifications")
    public BaseResponse toggleGeofenceNotifications(@PathVariable Long id) throws Exception{

        return geofenceService.toggleGeofenceNotifications(id);

    }

    @PatchMapping("/{id}/status")
    public BaseResponse toggleGeofenceStatus(@PathVariable Long id) throws Exception{

        return geofenceService.toggleGeofenceStatus(id);

    }


    @PostMapping("/add-people")
    public BaseResponse addPeople(@RequestBody UserGeofeceRequestDTO userGeofeceRequestDTO) throws Exception {

        return userGeofenceService.addPeopleInGeofence(userGeofeceRequestDTO);

    }


    @PostMapping("/accept-request/{id}")
    public BaseResponse acceptRequest(@PathVariable Long id) throws Exception {

        return userGeofenceService.acceptGeofenceRequest(id);

    }

    @PostMapping("/reject-request/{id}")
    public BaseResponse rejectRequest(@PathVariable Long id) throws Exception {

        return userGeofenceService.rejectGeofenceRequest(id);

    }

    @GetMapping("/get-people")
    public BaseResponse getPeopleInGeofence(@RequestParam Long geofenceId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) throws Exception {

        return userGeofenceService.getAllUsersInGeofence(geofenceId, page, size);

    }
}