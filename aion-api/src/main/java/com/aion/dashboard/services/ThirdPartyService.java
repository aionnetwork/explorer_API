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
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyService {

    private CirculatingSupplyJPARepository circulatingSupply;

    private NavigableMap<Instant, BigInteger> circulatingSupplyMap;

    @Autowired
    public ThirdPartyService(
        CirculatingSupplyJPARepository circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    @Cacheable(CacheConfig.CIRCULATING_SUPPLY)
    public String getCirculatingSupply() throws IOException {
        if (circulatingSupplyMap == null) {
            List<CirculatingSupply> circulatingSupplies = circulatingSupply.findAll();//this fails on test nets
            TreeMap<Instant, BigInteger> temp= new TreeMap<>(Instant::compareTo);
            for (CirculatingSupply supply : circulatingSupplies) {//read the table and store the contents in memory
                temp.put(supply.getStartDate().toInstant(), supply.getSupply().toBigInteger());
            }
            circulatingSupplyMap = Collections.unmodifiableNavigableMap(temp);// store in an immutable map
        }
        if(circulatingSupplyMap.isEmpty()) return "";
        else return circulatingSupplyMap.floorEntry(Instant.now()).getValue().toString(); //use floor entry to return the appropriate value
    }
}
