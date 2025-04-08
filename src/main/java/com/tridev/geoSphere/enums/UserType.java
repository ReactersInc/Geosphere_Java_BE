package com.tridev.geoSphere.enums;

import org.apache.coyote.BadRequestException;

public enum UserType {

    ADMIN(1, "Admin"),
    AGENT(2, "Agent"),
    MANAGER(4, "Manager"),
    USER(3, "User");


    private final int value;
    private final String description;

    UserType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static String getDescriptionByValue(int value) throws BadRequestException {
        for (UserType userType : UserType.values()) {
            if (userType.getValue() == value) {
                return userType.getDescription();
            }
        }
        throw new BadRequestException("something went wrong with the value "+ value);
    }

}






