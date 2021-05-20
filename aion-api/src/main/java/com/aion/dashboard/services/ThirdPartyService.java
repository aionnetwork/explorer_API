package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.CirculatingSupply;
import com.aion.dashboard.repositories.CirculatingSupplyJPARepository;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

//@Component
public class ThirdPartyService {

//    private CirculatingSupplyJPARepository circulatingSupply;
//
//    private NavigableMap<Instant, BigInteger> circulatingSupplyMap;
//
//    @Autowired
//    public ThirdPartyService(
//        CirculatingSupplyJPARepository circulatingSupply) {
//        this.circulatingSupply = circulatingSupply;
//    }


}
