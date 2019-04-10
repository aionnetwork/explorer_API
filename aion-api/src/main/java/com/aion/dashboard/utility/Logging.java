package com.aion.dashboard.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    private Logging(){
        throw new UnsupportedOperationException("Cannot create an instance of Logging");
    }
    private static Logger logger = LoggerFactory.getLogger("general");
    private static final String LOG_DELIMITER= "======================================================================================";
    public static void traceLogStartAndEnd(ZonedDateTime startZDT, ZonedDateTime endZDT, String s) {
        if (logger.isTraceEnabled()) {
            logger.trace(LOG_DELIMITER);
            logger.trace(s);
            logger.trace("Start: =[{}]", startZDT.format(DateTimeFormatter.ISO_DATE_TIME));
            logger.trace("End: =[{}]", endZDT.format(DateTimeFormatter.ISO_DATE_TIME));
            logger.trace(LOG_DELIMITER);
        }
    }


    public static void traceException(Exception e){
        traceException(e, "");
    }



    public static void traceException(Exception e, String msg){
        if (logger.isTraceEnabled()){
            logger.trace(LOG_DELIMITER);
            logger.trace(msg, e);
            logger.trace(LOG_DELIMITER);
        }
    }
}
