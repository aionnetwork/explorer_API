package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.view.Result;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

abstract class Mapper<T, U> {

    public abstract U makeDTO(T t);

    private List<U> makeDTOList(List<T> ts){
        return ts.stream().map(this::makeDTO).collect(Collectors.toList());
    }

    public Result<U> makeResult(T t){
        return Result.from(makeDTO(t));
    }

    public Result<U> makeResult(List<T> ts){
        return Result.from(makeDTOList(ts));
    }

    public Result<U> makeResult(Page<T> tpage){
        return Result.from(makeDTOList(tpage.getContent()), tpage);
    }

    public Result<U> makeResult(Page<T> tPage, long startTime, long endTime){
        return Result.from(makeDTOList(tPage.getContent()), tPage, startTime, endTime);
    }
}
