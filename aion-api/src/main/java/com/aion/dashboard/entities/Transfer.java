package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Transfer")
public class Transfer {

    @Id
    private Long transferId;
    private Long blockNumber;
    private Long transactionId;
    private Long transferTimestamp;
    private String toAddr;
    private String fromAddr;
    private String contractAddr;
    private BigDecimal tknValue;
    private String operatorAddr;

    public Long getTransferId()        { return transferId;        }
    public Long getBlockNumber()       { return blockNumber;       }
    public Long getTransactionId()     { return transactionId;     }
    public Long getTransferTimestamp() { return transferTimestamp; }
    public String getToAddr()          { return toAddr;            }
    public String getFromAddr()        { return fromAddr;          }
    public String getContractAddr()    { return contractAddr;      }
    public String getOperatorAddr()    { return operatorAddr;      }
    public BigDecimal getTknValue()    { return tknValue;          }

    public void setTransferId(Long transferId)               { this.transferId        = transferId;        }
    public void setBlockNumber(Long blockNumber)             { this.blockNumber       = blockNumber;       }
    public void setTransactionId(Long transactionId)         { this.transactionId     = transactionId;     }
    public void setTransferTimestamp(Long transferTimestamp) { this.transferTimestamp = transferTimestamp; }
    public void setToAddr(String toAddr)                     { this.toAddr            = toAddr;            }
    public void setFromAddr(String fromAddr)                 { this.fromAddr          = fromAddr;          }
    public void setContractAddr(String contractAddr)         { this.contractAddr      = contractAddr;      }
    public void setOperatorAddr(String operatorAddr)         { this.operatorAddr      = operatorAddr;      }
    public void setTknValue(BigDecimal tknValue)             { this.tknValue          = tknValue;          }

    public static Transfer toObject(JSONObject jsonObject) {
        Transfer transfer = new Transfer();

        transfer.setToAddr(jsonObject.getString("toAddr"));
        transfer.setFromAddr(jsonObject.getString("fromAddr"));
        transfer.setTransferId(jsonObject.getLong("transferId"));
        transfer.setContractAddr(jsonObject.getString("contractAddr"));
        transfer.setTransactionId(jsonObject.getLong("transactionId"));
        transfer.setTknValue(new BigDecimal(jsonObject.getLong("tknValue")));
        transfer.setTransferTimestamp(jsonObject.getLong("transferTimestamp"));
        transfer.setOperatorAddr(jsonObject.getString("operatorAddr"));

        return transfer;
    }
}
