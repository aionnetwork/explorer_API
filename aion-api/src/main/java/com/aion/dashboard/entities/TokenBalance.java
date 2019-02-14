package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "TokenBalance")
public class TokenBalance {

    @Id
    private String holderAddr;
    private String contractAddr;
    private BigDecimal tknBalance;
    private Long blockNumber;

    public String getContractAddr()   { return contractAddr;   }
    public String getHolderAddr()     { return holderAddr;     }
    public BigDecimal getTknBalance() { return tknBalance;     }
    public Long getBlockNumber()      { return blockNumber;    }

    public void setContractAddr(String contractAddr) { this.contractAddr = contractAddr;   }
    public void setHolderAddr(String holderAddr)     { this.holderAddr   = holderAddr;     }
    public void setTknBalance(BigDecimal tknBalance) { this.tknBalance   = tknBalance;     }
    public void setBlockNumber(Long blockNumber)     { this.blockNumber  = blockNumber;    }

    public static TokenBalance toObject(JSONObject jsonObject) {
        TokenBalance tokenBalance = new TokenBalance();

        tokenBalance.setHolderAddr(jsonObject.getString("holderAddr"));
        tokenBalance.setBlockNumber(jsonObject.getLong("blockNumber"));
        tokenBalance.setContractAddr(jsonObject.getString("contractAddr"));
        tokenBalance.setTknBalance(new BigDecimal(jsonObject.getLong("tknBalance")));

        return tokenBalance;
    }
}
