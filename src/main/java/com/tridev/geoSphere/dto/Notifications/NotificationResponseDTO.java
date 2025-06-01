package com.tridev.geoSphere.dto.Notifications;

import com.tridev.geoSphere.entities.sql.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private List<NotificationEntity> notification;
    private Integer pageNo;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private String sortOrder;
}
