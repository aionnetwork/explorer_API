package com.aion.dashboard.entities;

import com.aion.dashboard.repositories.TokenJpaRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenTest {

    @Autowired
    TokenJpaRepository tokenJpaRepository;

    Token token = new Token();

    @BeforeAll
    void getToken() {
//        token = tokenJpaRepository.findAllTokens(PageRequest.of(0, 100, new Sort()));
    }

    @Test
    void integrityTest() {

    }
}
