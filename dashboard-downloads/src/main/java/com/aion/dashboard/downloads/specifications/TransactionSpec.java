package com.aion.dashboard.downloads.specifications;

import com.aion.dashboard.downloads.entities.Transaction;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpec {
    private TransactionSpec() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    private static Specification<Transaction> isFrom(String addr) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fromAddr"), addr);
    }

    private static Specification<Transaction> isTo(String addr) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("toAddr"), addr);
    }

    private static Specification<Transaction> yearBetween(int yearStart, int yearEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), yearStart, yearEnd);
    }

    private static Specification<Transaction> yearEqual(int year){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    private static Specification<Transaction> monthBetween(int monthStart, int monthEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("month"), monthStart, monthEnd);
    }

    private static Specification<Transaction> timestampBetween(long timestampStart, long timestampEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
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
}
