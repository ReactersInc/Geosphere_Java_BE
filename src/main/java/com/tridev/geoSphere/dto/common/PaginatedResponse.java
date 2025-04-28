package com.tridev.geoSphere.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private List<T> list;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
