package com.tridev.geoSphere.enums;

public enum Status {

    ACTIVE(1),
    INACTIVE(2),
    REDO(3),
    DRAFT(4),
    PENDING(5),
    APPROVED(6),
    //loanApplication statuses
    CREATED(7),
    INITIATED(8),
    ACCEPTED(11),
    DEACTIVATED(14),
    DELETED(15),
    REJECTED(16),
    CANCELED(17);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static Status fromValue(Integer value) {
        for (Status b : Status.values()) {
            if (String.valueOf(b.value).equals(value+"")) {
                return b;
            }
        }
        return null;
    }

}
