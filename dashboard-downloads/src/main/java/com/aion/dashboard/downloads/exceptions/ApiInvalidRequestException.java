package com.aion.dashboard.downloads.exceptions;

public class ApiInvalidRequestException extends Exception {
    public ApiInvalidRequestException(String reason) {
        super(reason);
    }
}
