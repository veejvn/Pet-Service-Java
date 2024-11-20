package com.group.pet_service.enums;

//public enum Role {
//    USER(0),
//    STAFF(0),
//    ADMIN(0);
//    private final int value;
//
//    Role(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public static Role fromValue(int value) {
//        for (Role status : Role.values()) {
//            if (status.getValue() == value) {
//                return status;
//            }
//        }
//        throw new IllegalArgumentException("Invalid status value: " + value);
//    }
//
//}

public enum RoleEnum {
    ADMIN,
    STAFF,
    USER
}