package com.tridev.geoSphere.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionStatusDTO {

    private boolean requestSent;
    private boolean requestAccepted;

    public String getStatus() {
        if (requestAccepted) return "CONNECTED";
        if (requestSent) return "REQUEST_SENT";
        return "NOT_CONNECTED";
    }
}
