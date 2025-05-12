package com.tridev.geoSphere.dto.AppUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCheckResponse {
    private final boolean forceUpdate;
    private final String latestVersion;
    private final String updateType;
}

