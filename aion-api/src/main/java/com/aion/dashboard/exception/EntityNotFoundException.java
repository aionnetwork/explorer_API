package com.aion.dashboard.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Could not find entity with id.")
public class EntityNotFoundException extends RuntimeException
{
    static final long serialVersionUID = -3387416993334229948L;


    public EntityNotFoundException(String message)
    {
        super(message);
    }
    public EntityNotFoundException()
    {
        super();
    }

}