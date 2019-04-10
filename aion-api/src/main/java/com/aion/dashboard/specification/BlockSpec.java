package com.aion.dashboard.specification;

import com.aion.dashboard.entities.Block;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class BlockSpec {

    public static Specification<Block> yearBetween(int yearStart, int yearEnd){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), yearStart, yearEnd);
    }

    public static Specification<Block> monthBetween(int monthStart, int monthEnd){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("month"), monthStart, monthEnd);
    }

    public static Specification<Block> monthEqual(int month){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("month"), month);
    }


    public static Specification<Block> yearEqual(int year){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<Block> timeStampBetween(long start, long end ){
        return  (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get("blockTimestamp"), start, end);
    }


    public static Specification<Block> checkTime(int year, int monthStart, int monthEnd,  long start, long end){
        return yearEqual(year).and(monthBetween(monthStart, monthEnd)).and(timeStampBetween(start, end));
    }

    public static Specification<Block> checkTime(int yearStart, int yearEnd,  long start, long end){
        return yearBetween(yearStart, yearEnd).and(timeStampBetween(start, end));
    }

    public static Specification<Block> checkTime(ZonedDateTime zdtStart, ZonedDateTime zdtEnd){
        return timeStampBetween(zdtStart.toEpochSecond(), zdtEnd.toEpochSecond())
                .and(yearMonth(zdtStart).or(yearMonth(zdtEnd)));
    }

    private static Specification<Block> yearMonth(ZonedDateTime zdt){
        return yearEqual(zdt.getYear()).and(monthEqual(zdt.getMonthValue()));
    }


    public static Specification<Block> isMiner(String minerAddress){
        return  (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("minerAddress"), minerAddress);
    }
}
