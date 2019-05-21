package com.aion.dashboard.services;

import com.aion.dashboard.entities.ParserState;

import java.util.Optional;

public interface ParserStateService {
    Optional<ParserState> findById(int id);
}
