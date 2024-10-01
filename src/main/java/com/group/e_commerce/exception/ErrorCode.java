package com.group.e_commerce.exception;

public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "Uncategorize Exception"),
    INVALID_KEY(1001, "Invalid error key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003,"username must be at least 3 characters"),
    PASSWORD_INVALID(1004,"password must be at least 8 characters"),
    USER_NOTEXISTED(1005, "User is not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    TOKEN_UNGENERATED(1007, "Cannot generate token"),
    TOKEN_EXPIRED(1008, "Token is expired")
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
