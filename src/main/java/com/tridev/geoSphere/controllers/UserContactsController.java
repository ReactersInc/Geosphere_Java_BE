package com.tridev.geoSphere.controllers;

import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.services.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class UserContactsController {

    @Autowired
    private UserContactService userContactService;


    @GetMapping("/all")
    public BaseResponse getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {


        return userContactService.getUserContacts(page,size);
    }

}
