package com.aion.dashboard.downloads.specifications;

import com.aion.dashboard.downloads.entities.Block;
import org.springframework.data.jpa.domain.Specification;

public class BlockSpec {
    private BlockSpec() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    private static Specification<Block> yearBetween(int yearStart, int yearEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), yearStart, yearEnd);
    }

    private static Specification<Block> monthBetween(int monthStart, int monthEnd){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("month"), monthStart, monthEnd);
    }

    private static Specification<Block> yearEqual(int year){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    private static Specification<Block> timeStampBetween(long start, long end){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("blockTimestamp"), start, end);
    }

    public static Specification<Block> checkTime(int year, int monthStart, int monthEnd,  long start, long end){
        return yearEqual(year).and(monthBetween(monthStart, monthEnd)).and(timeStampBetween(start, end));
    }

    public static Specification<Block> checkTime(int yearStart, int yearEnd,  long start, long end){
        return yearBetween(yearStart, yearEnd).and(timeStampBetween(start, end));
    }

    public static Specification<Block> isMiner(String minerAddress){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("minerAddress"), minerAddress);
    }
}