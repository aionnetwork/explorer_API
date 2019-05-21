package com.aion.dashboard.services;

import com.aion.dashboard.entities.TokenHolders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TokenHoldersService {

    Page<TokenHolders> findAllByContractAddr(String contractAddr, Pageable pageable);

    List<TokenHolders> findByHolderAddr(String holderAddr);

    TokenHolders findByContractAddrAndHolderAddr(String contractAddr, String holderAddr);

    Long countByContractAddr(String contractAddr);


}
