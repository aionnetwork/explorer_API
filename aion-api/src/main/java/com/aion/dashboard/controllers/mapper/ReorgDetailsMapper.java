package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.ReorgDetailsDTO;
import com.aion.dashboard.entities.ReorgDetails;

public class ReorgDetailsMapper extends Mapper<ReorgDetails, ReorgDetailsDTO> {
    private static ReorgDetailsMapper mapper = new ReorgDetailsMapper();
    private ReorgDetailsMapper(){

    }

    public static ReorgDetailsMapper getInstance() {
        return mapper;
    }

    @Override
    ReorgDetailsDTO makeDTO(ReorgDetails reorgDetails) {
        return new ReorgDetailsDTO(reorgDetails.getBlockNumber(),
                reorgDetails.getServerTimestamp(),
                reorgDetails.getBlockDepth(),
                reorgDetails.getAffectedAddresses(),
                reorgDetails.getNumberOfAffectedTransactions());
    }
}
