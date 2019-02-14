package com.aion.dashboard.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BlockMap")
public class BlockMap {
	
	@Id
	private String blockHash;
	private Long blockNumber;
	
	public String getBlockHash() { return blockHash; }
	public Long getBlockNumber() { return blockNumber; }

	public void setBlockHash(String blockHash)   { this.blockHash   = blockHash;   }
	public void setBlockNumber(Long blockNumber) { this.blockNumber = blockNumber; }
}
