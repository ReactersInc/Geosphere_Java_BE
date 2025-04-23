package com.tridev.geoSphere.dto.userContacts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContactsRequestDTO {

    private Long userId;
    private Long contactUserId;

}
