package com.aion.dashboard.specification;

import com.aion.dashboard.entities.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class TransactionSpec {


    public static Specification<Transaction> isFrom(String addr) {
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fromAddr"), addr);
    }


    public static Specification<Transaction> isTo(String addr) {
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("toAddr"), addr);
    }


    public static Specification<Transaction> yearBetween(int yearStart, int yearEnd){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), yearStart, yearEnd);
    }

    public static Specification<Transaction> yearEqual(int year){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<Transaction> monthEqual(int month){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("month"), month);
    }



    public static Specification<Transaction> monthBetween(int monthStart, int monthEnd){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("month"), monthStart, monthEnd);
    }

    public static Specification<Transaction> timestampBetween(long timestampStart, long timestampEnd){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("blockTimestamp"), timestampStart, timestampEnd);

    }


    public static Specification<Transaction> hasAddr(String addr){
        return isFrom(addr).or(isTo(addr));
    }

    public static Specification<Transaction> checkTime(int year, int monthStart, int monthEnd, long timestampStart, long timeStampEnd){
        return yearEqual(year).and(monthBetween(monthStart, monthEnd)).and(timestampBetween(timestampStart, timeStampEnd));
    }

    public static Specification<Transaction> checkTime(int yearStart, int yearEnd,  long timestampStart, long timeStampEnd){
        return yearBetween(yearStart, yearEnd).and(timestampBetween(timestampStart, timeStampEnd));
    }

    private static Specification<Transaction> yearMonth(ZonedDateTime zdt){
        return yearEqual(zdt.getYear()).and(monthEqual(zdt.getMonthValue()));
    }


    public static Specification<Transaction> checkTime(ZonedDateTime zdtStart, ZonedDateTime zdtEnd){

        return timestampBetween(zdtStart.toEpochSecond(), zdtEnd.toEpochSecond())
                .and(yearMonth(zdtStart).or(yearMonth(zdtEnd)));
    }
}