package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;

@Entity
@Table(name="Block")
public class Block {

	@Id
	private Long blockNumber;
	private Long nrgConsumed;
	private Long nrgLimit;
	private Long size;
	private Long blockTimestamp;
	private Long numTransactions;
	private Long blockTime;
	private Long transactionId;
	private String blockHash;
	private String minerAddress;
	private String parentHash;
	private String receiptTxRoot;
	private String stateRoot;
	private String txTrieRoot;
	private String extraData;
	private String nonce;					// Byte Array
	private String bloom;
	private String solution;
	private String difficulty;				// Long
	private String totalDifficulty;			// Long
	private String transactionList;
	private String nrgReward;				// Long

	@Transient
	private BigInteger blockReward;

	public Long getBlockNumber()       { return blockNumber;     }
	public Long getNrgConsumed()       { return nrgConsumed;     }
	public Long getNrgLimit()          { return nrgLimit;        }
	public Long getSize()              { return size;            }
	public Long getBlockTimestamp()    { return blockTimestamp;  }
	public Long getNumTransactions()   { return numTransactions; }
	public Long getBlockTime()         { return blockTime;       }
	public Long getTransactionId()     { return transactionId;   }
	public String getBlockHash()       { return blockHash;       }
	public String getMinerAddress()    { return minerAddress;    }
	public String getParentHash()      { return parentHash;      }
	public String getTransactionList() { return transactionList; }
	public String getReceiptTxRoot()   { return receiptTxRoot;   }
	public String getStateRoot()       { return stateRoot;       }
	public String getTxTrieRoot()      { return txTrieRoot;      }
	public String getExtraData()       { return extraData;       }
	public String getNonce()           { return nonce;           }
	public String getBloom()           { return bloom;           }
	public String getSolution()        { return solution;        }
	public String getDifficulty()      { return difficulty;      }
	public String getTotalDifficulty() { return totalDifficulty; }
	public String getNrgReward()       { return nrgReward;       }

    public void setBlockNumber(Long blockNumber)           { this.blockNumber     = blockNumber;     }
    public void setNrgConsumed(Long nrgConsumed)           { this.nrgConsumed     = nrgConsumed;     }
    public void setNrgLimit(Long nrgLimit)                 { this.nrgLimit        = nrgLimit;        }
    public void setSize(Long size)                         { this.size            = size;            }
    public void setBlockTimestamp(Long blockTimestamp)     { this.blockTimestamp  = blockTimestamp;  }
    public void setNumTransactions(Long numTransactions)   { this.numTransactions = numTransactions; }
    public void setBlockTime(Long blockTime)               { this.blockTime       = blockTime;       }
    public void setTransactionId(long transactionId)       { this.transactionId   = transactionId;   }
    public void setMinerAddress(String minerAddress)       { this.minerAddress    = minerAddress;    }
    public void setTransactionList(String transactionList) { this.transactionList = transactionList; }
    public void setBlockHash(String blockHash)             { this.blockHash       = blockHash;       }
    public void setParentHash(String parentHash)           { this.parentHash      = parentHash;      }
    public void setReceiptTxRoot(String receiptTxRoot)     { this.receiptTxRoot   = receiptTxRoot;   }
    public void setStateRoot(String stateRoot)             { this.stateRoot       = stateRoot;       }
    public void setTxTrieRoot(String txTrieRoot)           { this.txTrieRoot      = txTrieRoot;      }
    public void setExtraData(String extraData)             { this.extraData       = extraData;       }
    public void setNonce(String nonce)                     { this.nonce           = nonce;           }
    public void setBloom(String bloom)                     { this.bloom           = bloom;           }
    public void setSolution(String solution)               { this.solution        = solution;        }
    public void setDifficulty(String difficulty)           { this.difficulty      = difficulty;      }
    public void setTotalDifficulty(String totalDifficulty) { this.totalDifficulty = totalDifficulty; }
	public void setNrgReward(String nrgReward)             { this.nrgReward       = nrgReward;       }



	public BigInteger getBlockReward() {
		return blockReward;
	}
	public void setBlockReward(BigInteger blockReward) {
		this.blockReward = blockReward;
	}

	public static Block toObject(JSONObject jsonObject)  {
		Block block = new Block();

		block.setTotalDifficulty(jsonObject.getString("totalDifficulty"));
		block.setNumTransactions(jsonObject.getLong("numTransactions"));
		block.setBlockTimestamp(jsonObject.getLong("blockTimestamp"));
		block.setReceiptTxRoot(jsonObject.getString("receiptTxRoot"));
		block.setMinerAddress(jsonObject.getString("minerAddress"));
		block.setTransactionId(jsonObject.getLong("transactionId"));
		block.setBlockNumber(jsonObject.getLong("blockNumber"));
		block.setNrgConsumed(jsonObject.getLong("nrgConsumed"));
		block.setParentHash(jsonObject.getString("parentHash"));
		block.setTxTrieRoot(jsonObject.getString("txTrieRoot"));
		block.setDifficulty(jsonObject.getString("difficulty"));
		block.setBlockHash(jsonObject.getString("blockHash"));
		block.setStateRoot(jsonObject.getString("stateRoot"));
		block.setExtraData(jsonObject.getString("extraData"));
		block.setNrgReward(jsonObject.getString("nrgReward"));
		block.setBlockTime(jsonObject.getLong("blockTime"));
		block.setSolution(jsonObject.getString("solution"));
		block.setNrgLimit(jsonObject.getLong("nrgLimit"));
		block.setNonce(jsonObject.getString("nonce"));
		block.setBloom(jsonObject.getString("bloom"));
		block.setSize(jsonObject.getLong("size"));



		block.setBlockReward(jsonObject.getBigInteger("blockReward"));
		return block;
	}
}
