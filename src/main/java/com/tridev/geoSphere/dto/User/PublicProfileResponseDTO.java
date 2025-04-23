package com.tridev.geoSphere.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicProfileResponseDTO {
     private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String photo;
    private Boolean requested;
    private Boolean connected;

}
