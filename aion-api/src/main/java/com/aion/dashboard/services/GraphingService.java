package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Graphing;
import com.aion.dashboard.repositories.GraphingJpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.core.io.NumberInput.parseBigDecimal;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public interface GraphingService {
    @Cacheable(CacheConfig.GRAPHING_INFO_BY_TIMESTAMP)
    String getGraphingInfo(int graphType) throws Exception;
}