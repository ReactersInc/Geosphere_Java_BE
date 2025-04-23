package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.dto.connectionRequest.ConnectionRequestDTO;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.ConnectionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connection-request")
public class ConnectionRequestController {

    @Autowired
    private ConnectionRequestService connectionRequestService;

    @PostMapping
    public BaseResponse createConnectionRequest(@RequestBody ConnectionRequestDTO connectionRequestDTO) throws Exception{
        return connectionRequestService.createConnectionRequest(connectionRequestDTO);
    }


    @GetMapping("/all")
    public BaseResponse getAllConnectionRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        return connectionRequestService.getAllConnectionRequestByTargetId(page, size);
    }


    @PutMapping("/accept/{id}")
    public BaseResponse acceptConnectionRequest(@PathVariable("id") Long id) throws Exception{
        return connectionRequestService.acceptConnectionRequest(id);
    }


    @PutMapping("/reject/{id}")
    public BaseResponse rejectConnectionRequest(@PathVariable("id") Long id) throws Exception{
        return connectionRequestService.rejectConnectionRequest(id);
    }




}
