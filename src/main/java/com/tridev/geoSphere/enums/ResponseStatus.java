package com.tridev.geoSphere.enums;

public enum ResponseStatus {

    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ResponseStatus fromValue(String text) {
        for (ResponseStatus status : ResponseStatus.values()) {
            if (String.valueOf(status.value).equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null; // You can throw an exception if needed
    }
}
