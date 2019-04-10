package com.aion.dashboard.downloads.specifications;

import com.aion.dashboard.downloads.entities.TokenTransfers;
import org.springframework.data.jpa.domain.Specification;

public class TransferSpec {
    private TransferSpec() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    private static Specification<TokenTransfers> isFrom(String addr) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fromAddr"), addr);
    }

    private static Specification<TokenTransfers> isTo(String addr) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("toAddr"), addr);
    }

    private static Specification<TokenTransfers> yearBetween(int yearStart, int yearEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), yearStart, yearEnd);
    }

    private static Specification<TokenTransfers> yearEqual(int year){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    private static Specification<TokenTransfers> monthBetween(int monthStart, int monthEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("month"), monthStart, monthEnd);
    }

    private static Specification<TokenTransfers> timestampBetween(long timestampStart, long timestampEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("transferTimestamp"), timestampStart, timestampEnd);

    }

    public static Specification<TokenTransfers> tokenIs(String tokenAddress) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("contractAddr"), tokenAddress);
    }

    public static Specification<TokenTransfers> hasAddr(String addr){
        return isFrom(addr).or(isTo(addr));
    }

    public static Specification<TokenTransfers> checkTime(int year, int monthStart, int monthEnd, long timestampStart, long timeStampEnd){
        return yearEqual(year).and(monthBetween(monthStart, monthEnd)).and(timestampBetween(timestampStart, timeStampEnd));
    }

    public static Specification<TokenTransfers> checkTime(int yearStart, int yearEnd,  long timestampStart, long timeStampEnd){
        return yearBetween(yearStart, yearEnd).and(timestampBetween(timestampStart, timeStampEnd));
    }
}
