package com.aion.dashboard.services;

import com.aion.dashboard.view.SearchResult;

public interface SearchService {

    /**
     * Performs a search of the configured database
     * @param key the key to be searched
     * @return the set of results with a key type pairing
     */
    SearchResult search(String key);
}
