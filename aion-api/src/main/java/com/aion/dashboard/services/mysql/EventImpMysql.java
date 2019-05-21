package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.Event;
import com.aion.dashboard.repositories.EventJpaRepository;
import com.aion.dashboard.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class EventImpMysql implements EventService {

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Override
    public Page<Event> findByTransactionHash(String transactionHash, Pageable pageable) {
        return eventJpaRepository.findByTransactionHash(transactionHash, pageable) ;
    }

    @Override
    public Page<Event> findByContractAddr(String contractAddr, Pageable pageable) {
        return eventJpaRepository.findByContractAddr(contractAddr, pageable) ;
    }
}
