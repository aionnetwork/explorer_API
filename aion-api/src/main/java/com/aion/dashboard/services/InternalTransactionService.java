package com.aion.dashboard.services;

import com.aion.dashboard.entities.InternalTransaction;
import com.aion.dashboard.exception.MissingArgumentException;
import com.aion.dashboard.repositories.InternalTransactionJpaRepository;
import com.aion.dashboard.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InternalTransactionService {
    private InternalTransactionJpaRepository txRepo;

    @Autowired
    public InternalTransactionService(InternalTransactionJpaRepository txRepo){
        this.txRepo = txRepo;
    }

    public List<InternalTransaction> findByID(String txHash, int index){
        InternalTransaction.Composite composite =new InternalTransaction.Composite(index, txHash);
        return txRepo.findById(composite).stream().collect(Collectors.toUnmodifiableList());
    }

    public List<InternalTransaction> findByTxHash(String txHash){
        return txRepo.findAllByTransactionHash(Utility.sanitizeHex(txHash));
    }
    private PageRequest pageRequest(int page, int size){
        return PageRequest.of(page, size, sortDesc());
    }

    private Sort sortDesc(){
        return new Sort( Sort.Direction.DESC,"blockNumber");
    }

    public Page<InternalTransaction> findByBlockNumber(long blockNumber, int page, int size){
        return txRepo.findAllByBlockNumber(blockNumber, pageRequest(page, size));
    }

    public Page<InternalTransaction> findByAddress(String addr, int page, int size){
        if (addr.isEmpty() || addr.isBlank()){
            throw new MissingArgumentException();
        }
        String sanitizedAddress = Utility.sanitizeHex(addr);
        return txRepo.findAllByToAddrOrFromAddrOrContractAddress(sanitizedAddress, sanitizedAddress, sanitizedAddress, pageRequest(page, size));
    }

    public List<InternalTransaction> findByContractAddress(String addr){
        if (addr.isEmpty() || addr.isBlank()){
            throw new MissingArgumentException();
        }return txRepo.findAllByContractAddress(addr);
    }

}
