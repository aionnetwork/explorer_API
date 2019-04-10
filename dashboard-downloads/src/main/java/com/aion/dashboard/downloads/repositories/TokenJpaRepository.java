package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.Token;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenJpaRepository extends PagingAndSortingRepository<Token, Long> {
    Token findByContractAddr(String contractAddr);
}
