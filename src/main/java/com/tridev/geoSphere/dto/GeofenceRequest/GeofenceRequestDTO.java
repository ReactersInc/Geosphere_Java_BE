package com.tridev.geoSphere.dto.GeofenceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceRequestDTO {

    private Long userId;
    private String recipientEmail;


}
