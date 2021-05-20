package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.CirculatingSupplyDTO;
import com.aion.dashboard.entities.CirculatingSupply;

public class CirculatingSupplyMapper extends Mapper<CirculatingSupply, CirculatingSupplyDTO>{
    private static final CirculatingSupplyMapper mapper = new CirculatingSupplyMapper();

    public static CirculatingSupplyMapper getMapper() {
        return mapper;
    }

    @Override
    CirculatingSupplyDTO makeDTO(CirculatingSupply circulatingSupply) {
        return new CirculatingSupplyDTO(circulatingSupply.getTimestamp()
                ,circulatingSupply.getSupply().toString()
                ,circulatingSupply.getBlockNumber());
    }
}
