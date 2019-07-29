package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.ContractDTO;
import com.aion.dashboard.entities.Contract;

public class ContractMapper extends Mapper<Contract, ContractDTO> {
    private static final ContractMapper mapper = new ContractMapper();

    public static ContractMapper getInstance() {
        return mapper;
    }
    private ContractMapper(){}
    @Override
    public ContractDTO makeDTO(Contract contract) {
        return new ContractDTO.ContractDTOBuilder()
                .setBlockNumber(contract.getBlockNumber())
                .setContractAddr(contract.getContractAddr())
                .setContractName(contract.getContractName())
                .setContractCreatorAddr(contract.getContractCreatorAddr())
                .setContractTxHash(contract.getContractTxHash())
                .setDeployTimestamp(contract.getDeployTimestamp())
                .setType(contract.getType())
                .createContractDTO();
    }
}
