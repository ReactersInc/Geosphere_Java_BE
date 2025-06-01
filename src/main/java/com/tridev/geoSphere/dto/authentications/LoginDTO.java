package com.tridev.geoSphere.dto.authentications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String email;
    private  String password;
    private String appVersion;
    private Integer platformId;
    private LocalDateTime installedAt;
}
