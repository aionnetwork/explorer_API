package com.aion.dashboard.services;

import com.aion.dashboard.entities.ReorgDetails;
import com.aion.dashboard.repositories.ReorgDetailsJPARepository;
import com.aion.dashboard.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ReorgDetailsService {
    private ReorgDetailsJPARepository reorgDetailsJPARepository;

    @Autowired
    public ReorgDetailsService(ReorgDetailsJPARepository reorgDetailsJPARepository) {
        this.reorgDetailsJPARepository = reorgDetailsJPARepository;
    }

    public Page<ReorgDetails> findAll(Integer page, Integer size){
        return reorgDetailsJPARepository.findAll(PageRequest.of(page, Utility.limitPageSize(size)));
    }
}
