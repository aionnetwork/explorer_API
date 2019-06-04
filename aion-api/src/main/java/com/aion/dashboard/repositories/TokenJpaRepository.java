package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Token;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Repository
public interface TokenJpaRepository extends PagingAndSortingRepository<Token, Long> {
    Page<Token> findByYearAndMonthBetweenAndCreationTimestampBetween(int year, int startMonth, int endMonth, long start, long end, Pageable pageable);
    Page<Token> findByYearBetweenAndCreationTimestampBetween(int startYear, int endYear, long start, long end, Pageable pageable);
    Page<Token> findBySymbol(String symbol, Pageable pageable);
    Page<Token> findByName(String name, Pageable pageable);
    Token findByContractAddr(String contractAddr);
    List<Token> findAllByNameOrSymbol(String name, String symbol);

    boolean existsByContractAddr(String contractAddr);
}
