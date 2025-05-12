package com.tridev.geoSphere.services;

import com.tridev.geoSphere.dto.AppUpdate.UpdateCheckResponse;
import com.tridev.geoSphere.entities.sql.AppPlatform;
import com.tridev.geoSphere.entities.sql.AppUpdateRequirement;
import com.tridev.geoSphere.repositories.sql.AppPlatformRepository;
import com.tridev.geoSphere.repositories.sql.AppUpdateRequirementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Slf4j
@Service
public class AppUpdateService {
    @Autowired
    private AppPlatformRepository appPlatformRepository;
    @Autowired
    private AppUpdateRequirementRepository appUpdateRequirementRepository;


    @Transactional(readOnly = true)
    public UpdateCheckResponse checkForUpdate(Long platformId, String currentVersion) {
        Optional<AppPlatform> platform = appPlatformRepository.findById(platformId);

        if (!platform.isPresent()) {
            throw new IllegalArgumentException("Invalid platform ID: " + platformId);
        }

        Optional<AppUpdateRequirement> requirement = appUpdateRequirementRepository.findByPlatform(platform.get());

        if (!requirement.isPresent()) {
            return new UpdateCheckResponse(false, currentVersion, "NONE");
        }

        AppUpdateRequirement updateRequirement = requirement.get();

        boolean forceUpdate = isVersionOlder(currentVersion, updateRequirement.getMinForceUpdateVersion());
        boolean optionalUpdate = isVersionOlder(currentVersion, updateRequirement.getMinOptionalUpdateVersion());
        log.info("here");
        if (forceUpdate) {
            return new UpdateCheckResponse(true, updateRequirement.getMinForceUpdateVersion(), "MANDATORY");
        } else if (optionalUpdate) {
            return new UpdateCheckResponse(false, updateRequirement.getMinOptionalUpdateVersion(), "OPTIONAL");
        }

        return new UpdateCheckResponse(false, currentVersion, "NONE");
    }

    private boolean isVersionOlder(String currentVersion, String requiredVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] requiredParts = requiredVersion.split("\\.");

        for (int i = 0; i < requiredParts.length; i++) {
            int current = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int required = Integer.parseInt(requiredParts[i]);

            if (current < required) {
                return true;
            } else if (current > required) {
                return false;
            }
        }
        return false;
    }
}
