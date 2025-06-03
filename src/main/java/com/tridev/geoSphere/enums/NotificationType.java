package com.tridev.geoSphere.enums;

public enum NotificationType {

    EXIT("EXIT"),
    ENTRY("ENTRY"),
    INVITATION("INVITATION"),
    CONTACT("CONTACT");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NotificationType fromValue(String text) {
        for (NotificationType status : NotificationType.values()) {
            if (String.valueOf(status.value).equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null; // You can throw an exception here if required
    }
}
