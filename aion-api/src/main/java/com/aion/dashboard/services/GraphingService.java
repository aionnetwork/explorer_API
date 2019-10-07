package com.aion.dashboard.services;

import static com.fasterxml.jackson.core.io.NumberInput.parseBigDecimal;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Graphing;
import com.aion.dashboard.repositories.GraphingJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class GraphingService {
    public enum GraphType {
        ACTIVE_ADDRESS_GROWTH("Active Address Growth"),
        TOP_MINER("Top Miner"),
        DIFFICULTY("Difficulty"),
        HASH_POWER("Hashing Power"),
        TRANSACTION_OVER_TIME("Transactions per hour"),
        BLOCK_TIME("Block Time"),
        BLOCKS_MINED("Blocks Mined"),
        POS_DIFFICULTY("Pos Difficulty"),
        POW_DIFFICULTY("Pow Difficulty"),
        POS_BLOCK_TIME("Pos avg block time"),
        POW_BLOCK_TIME("Pow avg block time");

        private String type;

        GraphType(String s) {
            type = s;
        }

        @Override
        public String toString() {
            return this.type;
        }

        public static GraphType getByType(String s){
            for (GraphType type:values()){
                if (type.type.equals(s)){
                    return type;
                }
            }
            throw new IllegalArgumentException("Illegal type");
        }
    }
    @Autowired
    private GraphingJpaRepository graphingJpaRepository;
    private final List<GraphType> graphTypes = List.of(GraphType.values());

    @Cacheable(CacheConfig.GRAPHING_INFO_BY_TIMESTAMP)
    public String getGraphingInfo(int graphType) throws Exception {
        if (graphType > graphTypes.size()) throw new IllegalArgumentException();
        List<Object> topMinerList;
        List<Graphing> graphingList;

        JSONObject graphingObject = new JSONObject();
        JSONArray graphingArray = new JSONArray();

        Long topMinerStartTime = ZonedDateTime.now().minusDays(7).withMinute(0).withSecond(0).toInstant().getEpochSecond();
        Long graphingStartTime = ZonedDateTime.now().minusYears(1).withMinute(0).withSecond(0).toInstant().getEpochSecond();
        Long endTime = ZonedDateTime.now().withMinute(0).withSecond(0).toInstant().getEpochSecond();

        // Graphing for Top Miner
        if(GraphType.TOP_MINER.ordinal() == graphType) {
            topMinerList = graphingJpaRepository.getTopMiner(topMinerStartTime, endTime);
            if (topMinerList != null && !topMinerList.isEmpty()) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                for (Object obj : topMinerList) {
                    String[] string = ow.writeValueAsString(obj)
                            .replace("\"", "")
                            .replace("[", "")
                            .replace("]", "")
                            .split(",");
                    JSONObject result = new JSONObject();
                    result.put("value", parseBigDecimal(string[0].trim()));
                    result.put("id", parseLong(string[1].trim()));
                    result.put("day", parseInt(string[2].trim()));
                    result.put("year", parseInt(string[3].trim()));
                    result.put("month", parseInt(string[4].trim()));
                    result.put("blockNumber", parseLong(string[5].trim()));
                    result.put("timestamp", parseLong(string[6].trim()));
                    result.put("graphType", string[7].trim());
                    result.put("detail", string[8].trim());
                    graphingArray.put(result);
                }
            }
        } else {

            // Graphing for Everything Else
            graphingList = graphingJpaRepository.getGraphInfo(graphingStartTime, endTime, graphTypes.get(graphType).type);
            if (graphingList != null && !graphingList.isEmpty()) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                for(Graphing graphing : graphingList) {
                    if (graphing.getValue().compareTo(BigDecimal.ZERO) >= 0) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(graphing));
                        graphingArray.put(result);
                    }
                }
            }
        }

        // If the ResultSet is Null
        if(graphingArray.length() == 0) {
            graphingArray = new JSONArray();
        }
        graphingObject.put("content", graphingArray);
        return graphingObject.toString();
    }
}