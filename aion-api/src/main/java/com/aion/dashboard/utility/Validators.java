package com.aion.dashboard.utility;

import com.aion.dashboard.exception.IncorrectArgumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validators {
    @Value("${com.aion.dashboard.validators.pageSizeLimit}")
    private int pageSizeLimit;

    public void validateSize( int size){
        if (size > pageSizeLimit){
            throw new IncorrectArgumentException(" less than "+pageSizeLimit);
        }
    }
}
