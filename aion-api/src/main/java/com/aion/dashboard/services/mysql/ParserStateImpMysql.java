package com.aion.dashboard.services.mysql;


import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.services.ParserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParserStateImpMysql implements ParserStateService {

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    @Override
    public Optional<ParserState> findById(int id) {
        return parserStateJpaRepository.findById(id);
    }
}
