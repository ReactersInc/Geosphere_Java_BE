package com.tridev.geoSphere.dto.userContacts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContactsResponseDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String photo;



}
