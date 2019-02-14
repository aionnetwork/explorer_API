package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Graphing;
import com.aion.dashboard.repository.GraphingJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.core.io.NumberInput.parseBigDecimal;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Component
public class GraphingService {
    private final List<String> GRAPH_TYPES = Arrays.asList("Active Address Growth", "Top Miner", "Difficulty", "Hashing Power", "Transactions per hour", "Block Time");

    @Autowired
    private GraphingJpaRepository graphingJpaRepository;

    @Cacheable(CacheConfig.GRAPHING_INFO_BY_TIMESTAMP)
    public String getGraphingInfo(int graphType) throws Exception {
        try {
            List<Object> topMinerList;
            List<Graphing> graphingList;

            Long topMinerStartTime = ZonedDateTime.now().minusDays(7).withMinute(0).withSecond(0).toInstant().getEpochSecond() * 1000;
            Long graphingStartTime = ZonedDateTime.now().minusYears(1).withMinute(0).withSecond(0).toInstant().getEpochSecond() * 1000;
            Long endTime = ZonedDateTime.now().withMinute(0).withSecond(0).toInstant().getEpochSecond() * 1000;

            // Graphing for Top Miner
            if(GRAPH_TYPES.get(graphType).equals("Top Miner")) {
                topMinerList = graphingJpaRepository.getTopMiner(topMinerStartTime, endTime);
                if (topMinerList != null && topMinerList.size() > 0) {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    JSONObject graphingObject = new JSONObject();
                    JSONArray graphingArray = new JSONArray();

                    for (int i = 0; i < topMinerList.size(); i++) {
                        String[] string = ow.writeValueAsString(topMinerList.get(i)).replace("[", "").replace("]", "").split(",");
                        JSONObject result = new JSONObject();
                        result.put("value", parseBigDecimal(string[0].trim()));
                        result.put("id", parseLong(string[1].trim()));
                        result.put("date", parseInt(string[2].trim()));
                        result.put("year", parseInt(string[3].trim()));
                        result.put("month", parseInt(string[4].trim()));
                        result.put("blockNumber", parseLong(string[5].trim()));
                        result.put("timestamp", parseLong(string[6].trim()));
                        result.put("graphType", string[7].replace("\"", "").trim());
                        result.put("detail", string[8].replace("\"", "").trim());
                        graphingArray.put(result);
                    }

                    graphingObject.put("content", graphingArray);
                    return graphingObject.toString();
                } else return new JSONObject().put("rel", JSONObject.NULL).toString();
            } else {

                // Graphing for Everything Else
                graphingList = graphingJpaRepository.getGraphInfo(graphingStartTime, endTime, GRAPH_TYPES.get(graphType));
                if (graphingList != null && graphingList.size() > 0) {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    JSONObject graphingObject = new JSONObject();
                    JSONArray graphingArray = new JSONArray();

                    for (int i = 0; i < graphingList.size(); i++) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(graphingList.get(i)));
                        graphingArray.put(result);
                    }

                    graphingObject.put("content", graphingArray);
                    return graphingObject.toString();
                } else return new JSONObject().put("rel", JSONObject.NULL).toString();
            }

        } catch(Exception e) { throw e; }
    }

    @Cacheable(CacheConfig.GRAPHING_LATEST_TIMESTAMP)
    public String getLatestTimestamp() { return graphingJpaRepository.getLatestTimestamp().toString(); }
}
