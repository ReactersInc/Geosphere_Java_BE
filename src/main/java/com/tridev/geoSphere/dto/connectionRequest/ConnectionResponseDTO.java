package com.tridev.geoSphere.dto.connectionRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionResponseDTO {
    private Long id;
    private Long requesterUserId;
    private Long targetUserId;
    private Integer status;
}
