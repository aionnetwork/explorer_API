package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.TokenHolders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenHoldersJpaRepository extends PagingAndSortingRepository<TokenHolders, Long> {

    List<TokenHolders> findAllByBlockNumberBetweenAndContractAddrOrderByBlockNumberDesc(Long start, Long end, String contractAddr);
    TokenHolders findByContractAddrAndHolderAddr(String contractAddr, String holderAddr);
}
