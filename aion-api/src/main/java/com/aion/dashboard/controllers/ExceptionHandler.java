package com.aion.dashboard.controllers;

import com.aion.dashboard.exception.MissingArgumentException;
import com.aion.dashboard.view.ErrorResults;
import com.aion.dashboard.view.ResultInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private <T extends ResultInterface> ResponseEntity<T> packageError(T body){
        //To be used to create custom headers
        return new ResponseEntity<>(body, HttpStatus.valueOf(body.getCode()) );
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ResultInterface> handleServerError(Exception e, WebRequest request){
        logException(e, request);
        return packageError(ErrorResults.SERVER_ERROR);
    }

    private void logException(Exception e, WebRequest request) {
        logger.error("Caught exception on call"+request.getContextPath()+": ", e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MissingArgumentException.class})
    protected ResponseEntity<ResultInterface> handleMissingArgument(MissingArgumentException e, WebRequest request){
        logException(e, request);
        return packageError(ErrorResults.MISSING_ARGUMENT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UnsupportedOperationException.class})
    protected ResponseEntity<ResultInterface> handleUnsupportedOperation(UnsupportedOperationException e, WebRequest request){
        logException(e, request);
        return packageError(ErrorResults.NOT_FOUND);
    }
}
