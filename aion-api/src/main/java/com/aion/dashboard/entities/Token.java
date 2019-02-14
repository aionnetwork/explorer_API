package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Token")
public class Token {

    @Id
    private String contractAddr;
    private String name;
    private String symbol;
    private String transactionHash;
    private String creatorAddress;
//    private String specialAddress;
    private BigDecimal granularity;
    private Long totalSupply;
    private Long liquidSupply;
    private Long creationTimestamp;

    public String getContractAddr()    { return contractAddr;      }
    public String getName()            { return name;              }
    public String getSymbol()          { return symbol;            }
    public String getTransactionHash() { return transactionHash;   }
    public String getCreatorAddress()  { return creatorAddress;    }
//    public String getSpecialAddress()  { return specialAddress;    }
    public BigDecimal getGranularity() { return granularity;       }
    public Long getTotalSupply()       { return totalSupply;       }
    public Long getLiquidSupply()      { return liquidSupply;      }
    public Long getCreationTimestamp() { return creationTimestamp; }

    public void setContractAddr(String contractAddr)         { this.contractAddr      = contractAddr;      }
    public void setName(String name)                         { this.name              = name;              }
    public void setSymbol(String symbol)                     { this.symbol            = symbol;            }
    public void setTransactionHash(String transactionHash)   { this.transactionHash   = transactionHash;   }
    public void setCreatorAddress(String creatorAddress)     { this.creatorAddress    = creatorAddress;    }
//    public void setSpecialAddress(String specialAddress)     { this.specialAddress    = specialAddress;    }
    public void setGranularity(BigDecimal granularity)       { this.granularity       = granularity;       }
    public void setTotalSupply(Long totalSupply)             { this.totalSupply       = totalSupply;       }
    public void setLiquidSupply(Long liquidSupply)           { this.liquidSupply      = liquidSupply;      }
    public void setCreationTimestamp(Long creationTimestamp) { this.creationTimestamp = creationTimestamp; }

    public static Token toObject(JSONObject jsonObject) {
        Token token = new Token();

        token.setName(jsonObject.getString("name"));
        token.setSymbol(jsonObject.getString("symbol"));
        token.setTotalSupply(jsonObject.getLong("totalSupply"));
        token.setLiquidSupply(jsonObject.getLong("liquidSupply"));
        token.setGranularity(new BigDecimal(jsonObject.getLong("granularity")));
        token.setContractAddr(jsonObject.getString("contractAddr"));
        token.setCreatorAddress(jsonObject.getString("creatorAddress"));
        token.setTransactionHash(jsonObject.getString("transactionHash"));
        token.setCreationTimestamp(jsonObject.getLong("creationTimestamp"));

        return token;
    }
}