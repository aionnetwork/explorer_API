package com.aion.dashboard.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResult implements ResultInterface {


    private final List<SearchResultValues> searchResults;

    @JsonCreator
    public SearchResult( @JsonProperty("searchResults") List<SearchResultValues> results) {
        this.searchResults = Collections.unmodifiableList(results);
    }

    @JsonGetter("searchResults")
    public List<SearchResultValues> getSearchResults() {
        return searchResults;
    }

    /**
     *
     * @param that the results to merge with
     * @return this if that is empty or a new search result containing the combined results
     */
    public SearchResult merge(SearchResult that) {
        if (that == null || that.searchResults.isEmpty()) {
            return this;
        } else {
            List<SearchResultValues> list = new ArrayList<>();

            list.addAll(this.searchResults);
            list.addAll(that.searchResults);
            return new SearchResult(list);
        }
    }

    /**
     * This class will never be instantiated with an error
     * @return null
     */
    @Override
    public Integer getCode() {
        return null;
    }

    public static class SearchResultValues implements Serializable {
        @JsonProperty("key")
        public final String key;
        @JsonProperty("type")
        public final String type;

        @JsonCreator
        public SearchResultValues(@JsonProperty("key") String key, @JsonProperty("type") String type) {
            this.key = key;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SearchResultValues)) return false;
            SearchResultValues that = (SearchResultValues) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, type);
        }
    }


    /**
     * Factory method
     * Builds the search result given the type and keys
     * @param type the type of result
     * @param keys the keys to be stored
     * @return the completed search result
     */
    public static SearchResult of(String type, String... keys){
        List<SearchResult.SearchResultValues> searchResults = Arrays.stream(keys)
                .map(k -> new SearchResult.SearchResultValues(k, type))
                .collect(Collectors.toList());
        return new SearchResult(searchResults);
    }
}
