package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.repositories.TokenHoldersJpaRepository;
import com.aion.dashboard.services.TokenHoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenHoldersImplMysql implements TokenHoldersService {

    @Autowired
    private TokenHoldersJpaRepository tokenHoldersJpaRepository;

    @Override
    public Page<TokenHolders> findAllByContractAddr(String contractAddr, Pageable pageable) {
        return tokenHoldersJpaRepository.findAllByContractAddr(contractAddr, pageable) ;
    }

    @Override
    public List<TokenHolders> findByHolderAddr(String holderAddr) {
        return tokenHoldersJpaRepository.findByHolderAddr(holderAddr);
    }

    @Override
    public TokenHolders findByContractAddrAndHolderAddr(String contractAddr, String holderAddr) {
        return tokenHoldersJpaRepository.findByContractAddrAndHolderAddr(contractAddr, holderAddr) ;
    }

    @Override
    public Long countByContractAddr(String contractAddr) {
        return tokenHoldersJpaRepository.countByContractAddr(contractAddr) ;
    }
}
