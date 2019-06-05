package com.aion.dashboard.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Result<T> implements ResultInterface {

    public static final Result<?> EMPTY_RESULT = new Result<>(Collections.emptyList(), null, null);
    private final List<T> content;
    private final Pageable page;
    private final Integer code;

    Result(List<T> content, ResultPageable page, Integer code) {
        this.content = content;
        this.page = page;
        this.code = code;
    }

    public static <T> Result<T> from(int code, T content) {
        return new Result<>(List.of(content), null, code);
    }

    public static <T> Result<T> from(T content) {
        return new Result<>(List.of(content), null, null);
    }

    public static <T> Result<T> from(List<T> content, ResultPageable page) {
        return new Result<>(content, page, null);
    }

    public static <T> Result<T> from(Page<T> page){
        return new Result<>(page.getContent(), ResultPageable.from(page), null);
    }

    public static <T> Result<T> from(Page<T> page, long start, long end){
        return new Result<>(page.getContent(), ResultPageable.fromPageAndTime(page, start, end), null);
    }

    public List<T> getContent() {
        return content;
    }

    public Pageable getPage() {
        return page;
    }

    public Integer getCode() {
        return code;
    }

    private interface Pageable{}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResultPageable implements Pageable{
        final Integer page;
        final Integer size;
        final Integer totalPages;
        final Integer totalElements;
        final Long start;
        final Long end;

        private ResultPageable(Integer page, Integer size, Integer totalPages, Integer totalElements, Long start, Long end) {
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.start = start;
            this.end = end;
        }

        private ResultPageable(Integer page, Integer size, Integer totalPages, Integer totalElements) {
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.start=null;
            this.end=null;
        }

        static ResultPageable from(Page page){
            return new ResultPageable(page.getNumber(), page.getSize(), page.getTotalPages(), page.getNumberOfElements());
        }

        static  ResultPageable fromPageAndTime(Page page, Long start, Long end){
            return new ResultPageable(page.getNumber(), page.getSize(), page.getTotalPages(), page.getNumberOfElements(), start, end);
        }
    }


}

