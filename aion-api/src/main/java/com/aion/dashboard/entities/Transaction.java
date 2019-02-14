package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Transaction")
public class Transaction {
	
	@Id
    private Long id;
    private Long blockNumber;
    private Long transactionIndex;
    private Long nrgConsumed;
    private Long nrgPrice;
    private String transactionHash;
    private String blockHash;
    private String fromAddr;
    private String toAddr;
    private String transactionTimestamp;        // Long
    private String blockTimestamp;              // Long
    private String value;                       // BigInteger
    private String transactionLog;
    private String data;
    private String nonce;                       // BigInteger
    private String txError;
    private String contractAddr;

    public Long getId()                     { return id;                   }
    public Long getBlockNumber()            { return blockNumber;          }
    public Long getTransactionIndex()       { return transactionIndex;     }
    public Long getNrgConsumed()            { return nrgConsumed;          }
    public Long getNrgPrice()               { return nrgPrice;             }
    public String getTransactionHash()      { return transactionHash;      }
    public String getContractAddr()         { return contractAddr;         }
    public String getTxError()              { return txError;              }
    public String getBlockHash()            { return blockHash;            }
    public String getFromAddr()             { return fromAddr;             }
    public String getToAddr()               { return toAddr;               }
    public String getTransactionTimestamp() { return transactionTimestamp; }
    public String getBlockTimestamp()       { return blockTimestamp;       }
    public String getValue()                { return value;                }
    public String getTransactionLog()       { return transactionLog;       }
    public String getNonce()                { return nonce;                }
    public String getData()                 { return data;                 }

    public void setId(Long id)                                       { this.id                   = id;                   }
    public void setBlockNumber(Long blockNumber)                     { this.blockNumber          = blockNumber;          }
    public void setTransactionIndex(Long transactionIndex)           { this.transactionIndex     = transactionIndex;     }
    public void setNrgConsumed(Long nrgConsumed)                     { this.nrgConsumed          = nrgConsumed;          }
    public void setNrgPrice(Long nrgPrice)                           { this.nrgPrice             = nrgPrice;             }
    public void setTransactionHash(String transactionHash)           { this.transactionHash      = transactionHash;      }
    public void setBlockHash(String blockHash)                       { this.blockHash            = blockHash;            }
    public void setFromAddr(String fromAddr)                         { this.fromAddr             = fromAddr;             }
    public void setToAddr(String toAddr)                             { this.toAddr               = toAddr;               }
    public void setTransactionTimestamp(String transactionTimestamp) { this.transactionTimestamp = transactionTimestamp; }
    public void setBlockTimestamp(String blockTimestamp)             { this.blockTimestamp       = blockTimestamp;       }
    public void setValue(String value)                               { this.value                = value;                }
    public void setTransactionLog(String transactionLog)             { this.transactionLog       = transactionLog;       }
    public void setData(String data)                                 { this.data                 = data;                 }
    public void setNonce(String nonce)                               { this.nonce                = nonce;                }
    public void setTxError(String txError)                           { this.txError              = txError;              }
    public void setContractAddr(String contractAddr)                 { this.contractAddr         = contractAddr;         }

    public static Transaction toObject(JSONObject jsonObject) {
        Transaction transaction = new Transaction();

        transaction.setId(jsonObject.getLong("id"));
        transaction.setData(jsonObject.getString("data"));
        transaction.setNonce(jsonObject.getString("nonce"));
        transaction.setValue(jsonObject.getString("value"));
        transaction.setToAddr(jsonObject.getString("toAddr"));
        transaction.setNrgPrice(jsonObject.getLong("nrgPrice"));
        transaction.setTxError(jsonObject.getString("txError"));
        transaction.setFromAddr(jsonObject.getString("fromAddr"));
        transaction.setBlockHash(jsonObject.getString("blockHash"));
        transaction.setNrgConsumed(jsonObject.getLong("nrgConsumed"));
        transaction.setContractAddr(jsonObject.getString("contractAddr"));
        transaction.setBlockTimestamp(jsonObject.getString("blockTimestamp"));
        transaction.setTransactionLog(jsonObject.getString("transactionLog"));
        transaction.setTransactionHash(jsonObject.getString("transactionHash"));
        transaction.setTransactionIndex(jsonObject.getLong("transactionIndex"));
        transaction.setTransactionTimestamp(jsonObject.getString("transactionTimestamp"));

        return transaction;
    }
}
