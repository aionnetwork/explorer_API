package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.InternalTransactionDTO;
import com.aion.dashboard.entities.InternalTransaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalTransactionMapperTest {

    @Test
    void makeDTO() {
        InternalTransactionMapper mapper = InternalTransactionMapper.getInstance();
        InternalTransaction entity = new InternalTransaction();
        entity.setBlockNumber(1L);
        entity.setTimestamp(1L);
        entity.setContractAddress("valid1");
        entity.setData("valid2");
        entity.setFromAddr("bob");
        entity.setToAddr("alice");
        entity.setTransactionHash("valid3");
        entity.setKind("create");
        entity.setInternalTransactionIndex(0);
        entity.setNonce(BigDecimal.valueOf(1));
        entity.setNrgLimit(BigDecimal.valueOf(2));
        entity.setNrgPrice(BigDecimal.valueOf(3));
        entity.setRejected(false);
        entity.setValue(BigDecimal.valueOf(4));

        InternalTransactionDTO dto = mapper.makeDTO(entity);

        assertEquals(dto.getBlockNumber(), entity.getBlockNumber());
        assertEquals(dto.getBlockTimestamp(), entity.getTimestamp());
        assertEquals(dto.getContractAddress(), entity.getContractAddress());
        assertEquals(dto.getData(), entity.getData());
        assertEquals(dto.getInternalTransactionIndex(), entity.getInternalTransactionIndex());
        assertEquals(dto.getFromAddr() , entity.getFromAddr());
        assertEquals(dto.getToAddr(), entity.getToAddr());
        assertEquals(dto.getTransactionHash(), entity.getTransactionHash());
        assertEquals(dto.getNonce(), entity.getNonce().toBigInteger().toString());
        assertEquals(dto.getRejected(), entity.isRejected());
        assertEquals(dto.getType(), entity.getKind());
    }
}