package com.aion.dashboard.datatransferobject;

public class ContractDTO {

    private String contractAddr;
    private String contractName;
    private String deploymentHash;
    private String contractCreatorAddr;
    private Long deploymentTimestamp;
    private Long deploymentBlockNumber;
    private String contractType;

    public ContractDTO(String contractAddr, String contractName, String deploymentHash, String contractCreatorAddr, Long deployTimestamp, Long blockNumber, String contractType) {
        this.contractAddr = contractAddr;
        this.contractName = contractName;
        this.deploymentHash = deploymentHash;
        this.contractCreatorAddr = contractCreatorAddr;
        this.deploymentTimestamp = deployTimestamp;
        this.deploymentBlockNumber = blockNumber;
        this.contractType = contractType;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getContractName() {
        return contractName;
    }

    public String getDeploymentHash() {
        return deploymentHash;
    }

    public String getContractCreatorAddr() {
        return contractCreatorAddr;
    }

    public Long getDeploymentTimestamp() {
        return deploymentTimestamp;
    }

    public Long getDeploymentBlockNumber() {
        return deploymentBlockNumber;
    }

    public String getContractType() {
        return contractType;
    }

    public static class ContractDTOBuilder {
        private String contractAddr;
        private String contractName;
        private String contractTxHash;
        private String contractCreatorAddr;
        private Long deployTimestamp;
        private Long blockNumber;
        private String type;

        public ContractDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public ContractDTOBuilder setContractName(String contractName) {
            this.contractName = contractName;
            return this;
        }

        public ContractDTOBuilder setContractTxHash(String contractTxHash) {
            this.contractTxHash = contractTxHash;
            return this;
        }

        public ContractDTOBuilder setContractCreatorAddr(String contractCreatorAddr) {
            this.contractCreatorAddr = contractCreatorAddr;
            return this;
        }

        public ContractDTOBuilder setDeployTimestamp(Long deployTimestamp) {
            this.deployTimestamp = deployTimestamp;
            return this;
        }

        public ContractDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public ContractDTOBuilder setType(String type) {
            if (type.equalsIgnoreCase("default")) {
                this.type = "FVM";
            } else {
                this.type = type.toUpperCase();
            }
            return this;
        }


        public ContractDTO createContractDTO() {
            return new ContractDTO(contractAddr, contractName, contractTxHash, contractCreatorAddr, deployTimestamp, blockNumber, type);
        }
    }

}
