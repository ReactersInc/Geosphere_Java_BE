package com.tridev.geoSphere.dto.Geofence;

import lombok.Data;

import java.util.List;

@Data
public class GeofencePaginationResponse {
    private int totalPages;
    private int pageNo;
    private int pageSize;
    private long totalRecords;
    private String sortOrder;
    private List<GeofenceResponseDTO> data;
}
