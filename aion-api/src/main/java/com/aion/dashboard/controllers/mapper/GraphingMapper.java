package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.GraphingDTO;
import com.aion.dashboard.entities.Graphing;

public class GraphingMapper extends Mapper<Graphing, GraphingDTO> {

    private static final GraphingMapper mapper = new GraphingMapper();

    public static GraphingMapper getInstance() {
        return mapper;
    }

    private GraphingMapper(){}
    @Override
    public GraphingDTO makeDTO(Graphing graphing) {
        return new GraphingDTO.GraphingDTOBuilder().setBlockNumber(graphing.getBlockNumber())
                .setDetail(graphing.getDetail())
                .setGraphType(graphing.getGraphType())
                .setTimestamp(graphing.getTimestamp())
                .setValue(graphing.getValue()).createGraphingDTO();
    }
}
