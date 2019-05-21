package com.aion.dashboard.services;

import com.aion.dashboard.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    Page<Event> findByTransactionHash(String transactionHash, Pageable pageable);
     Page<Event> findByContractAddr(String contractAddr, Pageable pageable);


}
