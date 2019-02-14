package com.aion.dashboard.exceptions;

public class ApiInvalidRequestException extends Exception {
    public ApiInvalidRequestException(String reason) {
        super(reason);
    }
}