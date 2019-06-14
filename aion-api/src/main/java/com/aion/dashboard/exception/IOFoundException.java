package com.aion.dashboard.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Could not find data.")
public class IOFoundException extends Exception
{
    static final long serialVersionUID = -3387516993334229448L;


    public IOFoundException(String message)
    {
        super(message);
    }

}