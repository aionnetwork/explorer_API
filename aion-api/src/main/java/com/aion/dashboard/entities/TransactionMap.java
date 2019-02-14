package com.aion.dashboard.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TransactionMap")
public class TransactionMap {
	
	@Id
	private String transactionHash;
	private Long id;

	public Long getId()                { return id;              }
	public String getTransactionHash() { return transactionHash; }

	public void setId(Long id)                             { this.id              = id;              }
	public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }
}
