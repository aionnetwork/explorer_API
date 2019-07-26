package com.aion.dashboard.datatransferobject;

import java.util.List;

public class EventDTO {

    private Long blockNumber;
    private Long eventTimestamp;
    private String transactionHash;
    private List<String> parameterList;
    private String contractAddr;
    private List<String> inputList;
    private String name;


    private EventDTO(Long blockNumber, Long eventTimestamp, String transactionHash, List<String> parameterList, String contractAddr, List<String> inputList, String name) {
        this.blockNumber = blockNumber;
        this.eventTimestamp = eventTimestamp;
        this.transactionHash = transactionHash;
        this.parameterList = parameterList;
        this.contractAddr = contractAddr;
        this.inputList = inputList;
        this.name = name;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public Long getEventTimestamp() {
        return eventTimestamp;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public List<String> getParameterList() {
        return parameterList;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public List<String> getInputList() {
        return inputList;
    }

    public String getName() {
        return name;
    }

    public static class EventDTOBuilder{

        private Long blockNumber;
        private Long eventTimestamp;
        private String transactionHash;
        private List<String> parameterList;
        private String contractAddr;
        private List<String> inputList;
        private String name;

        public EventDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public EventDTOBuilder setEventTimestamp(Long eventTimestamp) {
            this.eventTimestamp = eventTimestamp;
            return this;
        }

        public EventDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public EventDTOBuilder setParameterList(List<String> parameterList) {
            this.parameterList = parameterList;
            return this;
        }

        public EventDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public EventDTOBuilder setInputList(List<String> inputList) {
            this.inputList = inputList;
            return this;
        }

        public EventDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EventDTO createEventDTO(){
            return new EventDTO(blockNumber, eventTimestamp, transactionHash, parameterList, contractAddr, inputList, name);
        }
    }
}
