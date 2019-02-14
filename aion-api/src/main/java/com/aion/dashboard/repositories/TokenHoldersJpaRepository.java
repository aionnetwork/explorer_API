package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.TokenHolders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenHoldersJpaRepository extends PagingAndSortingRepository<TokenHolders, Long> {
//    @Query(value = "SELECT * FROM token_holders WHERE contract_addr = ?1 ORDER BY balance DESC", nativeQuery = true)
    Page<TokenHolders> findAllByContractAddr(String contractAddr, Pageable pageable);

    List<TokenHolders> findByHolderAddr(String holderAddr);

    TokenHolders findByContractAddrAndHolderAddr(String contractAddr, String holderAddr);

    Long countByContractAddr(String contractAddr);
}
