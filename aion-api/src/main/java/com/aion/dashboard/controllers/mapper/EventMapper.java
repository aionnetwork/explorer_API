package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.EventDTO;
import com.aion.dashboard.entities.Event;

import java.util.Arrays;
import java.util.List;

public class EventMapper extends Mapper<Event, EventDTO> {

    private static final EventMapper mapper = new EventMapper();

    public static EventMapper getInstance() {
        return mapper;
    }
    private EventMapper(){}
    @Override
    public EventDTO makeDTO(Event event) {
        List<String> parameterList = Arrays.asList(event.getParameterList().replaceAll("(|]|\\[)", "").split(","));
        List<String> inputList = Arrays.asList(event.getInputList().replaceAll("(|]|\\[)", "").split(","));
        return new EventDTO.EventDTOBuilder()
                .setBlockNumber(event.getBlockNumber())
                .setContractAddr(event.getContractAddr())
                .setEventTimestamp(event.getEventTimestamp())
                .setInputList(inputList)
                .setName(event.getName())
                .setParameterList(parameterList)
                .setTransactionHash(event.getTransactionHash())
                .createEventDTO();
    }
}
