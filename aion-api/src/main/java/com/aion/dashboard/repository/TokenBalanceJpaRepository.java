package com.aion.dashboard.repository;

import com.aion.dashboard.entities.TokenBalance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenBalanceJpaRepository extends PagingAndSortingRepository<TokenBalance, Long> {
    @Query(value = "SELECT COUNT(contract_addr) FROM token_balance WHERE contract_addr = ?1", nativeQuery = true)
    Long getTotalHolders(String contract_addr);
    @Query(value = "SELECT * FROM token_balance WHERE contract_addr = ?1 ORDER BY tkn_balance DESC", nativeQuery = true)
    Page<TokenBalance> getTopHoldersByContractAddr(String contract_addr, Pageable pageable);
    TokenBalance findByContractAddrAndHolderAddr(String contract_addr, String holder_addr);
    List<TokenBalance> findByHolderAddr(String holder_addr);
}
