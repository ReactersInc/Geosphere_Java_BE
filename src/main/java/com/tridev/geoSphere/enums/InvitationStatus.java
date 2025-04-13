package com.tridev.geoSphere.enums;

public enum InvitationStatus {

    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed");

    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static InvitationStatus fromValue(String text) {
        for (InvitationStatus status : InvitationStatus.values()) {
            if (String.valueOf(status.value).equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null; // You can throw an exception if needed
    }
}
