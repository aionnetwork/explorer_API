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
        else if (size<1){
            throw new IncorrectArgumentException(" greater than or equal to "+1);
        }
    }

    public void validatePage(int page){
        if (page<0){
            throw new IncorrectArgumentException(" greater than or equal to "+0);
        }
    }
}
