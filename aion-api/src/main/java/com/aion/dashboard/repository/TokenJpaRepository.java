package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenJpaRepository extends PagingAndSortingRepository<Token, Long> {
    @Query(value = "SELECT * FROM token ORDER BY creation_timestamp DESC", nativeQuery = true)
    Page<Token> findAllTokens(Pageable pageable);
    Page<Token> findBySymbol(String symbol, Pageable pageable);
    Page<Token> findByName(String name, Pageable pageable);
    Token findByContractAddr(String contract_addr);
}
