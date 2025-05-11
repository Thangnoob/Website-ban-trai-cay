package com.nhom15.orderservice.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private String code;
    private int status;
    public CustomException(String message, String errorCode, int status) {
        super(message);
        this.code = errorCode;
        this.status = status;
    }
}
