package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class GraphingDTO {
    private BigDecimal value;
    private String detail;
    private String graphType;
    private Long timestamp;
    private Long blockNumber;

    public BigDecimal getValue() {
        return value;
    }

    public String getDetail() {
        return detail;
    }

    public String getGraphType() {
        return graphType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public GraphingDTO(BigDecimal value, String detail, String graphType, Long timestamp, Long blockNumber) {
        this.value = value;
        this.detail = detail;
        this.graphType = graphType;
        this.timestamp = timestamp;
        this.blockNumber = blockNumber;
    }


    public static class GraphingDTOBuilder{
        private BigDecimal value;
        private String detail;
        private String graphType;
        private Long timestamp;
        private Long blockNumber;

        public GraphingDTOBuilder setValue(BigDecimal value) {
            this.value = value;
            return this;
        }


        public GraphingDTOBuilder setDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public GraphingDTOBuilder setGraphType(String graphType) {
            this.graphType = graphType;
            return this;
        }

        public GraphingDTOBuilder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public GraphingDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public GraphingDTO createGraphingDTO(){
            return new GraphingDTO(value, detail, graphType, timestamp, blockNumber);
        }
    }
}
