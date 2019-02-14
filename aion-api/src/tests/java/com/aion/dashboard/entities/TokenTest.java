package com.aion.dashboard.entities;

import com.aion.dashboard.repository.TokenJpaRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class TokenTest {

    @Autowired
    TokenJpaRepository tokenJpaRepository;

    Token token = new Token();

    @BeforeAll
    void getToken() {
//        token = tokenJpaRepository.findAllTokens(new PageRequest(0, 100, new Sort()));
    }

    @Test
    void integrityTest() {

    }
}
