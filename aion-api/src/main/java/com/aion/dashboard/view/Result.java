package com.aion.dashboard.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Result<T> implements ResultInterface {

    public static final Result<?> EMPTY_RESULT = new Result<>(Collections.emptyList(), null, null);
    private final List<T> content;
    private final ResultPageable page;
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

    public static <T> Result<T> from(List<T> content) {
        return new Result<>(content, null, null);
    }

    public static <T> Result<T> from(List<T> content,Page page) {
        return new Result<>(content, ResultPageable.from(page), null);
    }

    public static <T> Result<T> from(Page<T> page){
        return new Result<>(page.getContent(), ResultPageable.from(page), null);
    }

    public static <T> Result<T> from(List<T> content, Page<?> page, long start, long end){
        return new Result<>(content, ResultPageable.fromPageAndTime(page, start, end), null);
    }

    public List<T> getContent() {
        return content;
    }

    public ResultPageable getPage() {
        return page;
    }

    public Integer getCode() {
        return code;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResultPageable{
        final Integer page;
        final Integer size;
        final Integer totalPages;
        final long totalElements;
        final Long start;
        final Long end;

        public Integer getPage() {
            return page;
        }

        public Integer getSize() {
            return size;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public Long getStart() {
            return start;
        }

        public Long getEnd() {
            return end;
        }

        private ResultPageable(Integer page, Integer size, Integer totalPages, long totalElements, Long start, Long end) {
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.start = start;
            this.end = end;
        }

        private ResultPageable(Integer page, Integer size, Integer totalPages, long totalElements) {
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.start=null;
            this.end=null;
        }

        static ResultPageable from(Page page){
            return new ResultPageable(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        }

        static  ResultPageable fromPageAndTime(Page page, Long start, Long end){
            return new ResultPageable(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), start, end);
        }
    }


}

