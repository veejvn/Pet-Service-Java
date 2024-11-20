package com.group.pet_service.exception;

public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "Uncategorize Exception"),
    INVALID_KEY(1001, "Invalid error key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003,"username must be at least 3 characters"),
    PASSWORD_INVALID(1004,"password must be at least 8 characters"),
    USER_NOTEXISTED(1005, "User is not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    TOKEN_UNGENERATED(1007, "Cannot generate token"),
    TOKEN_EXPIRED(1008, "Token is expired"),
    VERIFICATION_FAILED(1009, "Verifying process failed"),
    VERIFICATION_EXPIRED(1010,"Verifying Code Expired"),
    SERVICE_EXISTED(1011,"This service existed in database"),
    SPECIES_EXISTED(1012,"This species existed in database"),
    SPECIES_NOT_EXISTED(1012,"This species not existed in database"),
    SERVICE_NOT_FOUND(1013,"This service does not existed in database"),
    NO_IMAGES_PROVIDED(1014, "No Image Found"),
    PET_NOT_FOUND(1015,"This service does not existed in database"),
    IMAGE_UPLOAD_FAILED(1016,"failed to upload images"),

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private  int code = 1000;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
