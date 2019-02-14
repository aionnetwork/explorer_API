package com.aion.dashboard.entities;

import javax.persistence.*;

@Entity
@Table(name="ParserState")
public class ParserState {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Long blockNumber;
	private Long transactionId;

	public void setId(Integer id) { this.id = id; }

	public Integer getId()         { return id;            }
	public Long getBlockNumber()   { return blockNumber;   }
	public Long getTransactionId() { return transactionId; }
}