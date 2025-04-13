package com.tridev.geoSphere.enums;

public enum NotificationStatus {

    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NotificationStatus fromValue(String text) {
        for (NotificationStatus status : NotificationStatus.values()) {
            if (String.valueOf(status.value).equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null; // You can throw an exception here if required
    }
}
