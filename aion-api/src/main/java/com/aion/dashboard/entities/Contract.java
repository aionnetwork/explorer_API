package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Contract")
public class Contract {

    @Id
    private String contractAddr;
    private String contractName;
    private String contractTxHash;
    private String contractCreatorAddr;
    private Long deployTimestamp;
    private Long blockNumber;

    public String getContractAddr()        { return contractAddr;        }
    public String getContractName()        { return contractName;        }
    public String getContractTxHash()      { return contractTxHash;      }
    public String getContractCreatorAddr() { return contractCreatorAddr; }
    public Long getDeployTimestamp()       { return deployTimestamp;     }
    public Long getBlockNumber()           { return blockNumber;         }

    public void setContractAddr(String contractAddr)               { this.contractAddr        = contractAddr;         }
    public void setContractName(String contractName)               { this.contractName        = contractName;         }
    public void setContractTxHash(String contractTxHash)           { this.contractTxHash      = contractTxHash;       }
    public void setContractCreatorAddr(String contractCreatorAddr) { this.contractCreatorAddr = contractCreatorAddr;  }
    public void setDeployTimestamp(Long deployTimestamp)           { this.deployTimestamp     = deployTimestamp;      }
    public void setBlockNumber(Long blockNumber)                   { this.blockNumber         = blockNumber;          }

    public static Contract toObject(JSONObject jsonObject) {
        Contract contract = new Contract();

        contract.setBlockNumber(jsonObject.getLong("blockNumber"));
        contract.setContractAddr(jsonObject.getString("contractAddr"));
        contract.setContractName(jsonObject.getString("contractName"));
        contract.setContractTxHash(jsonObject.getString("contractTxHash"));
        contract.setDeployTimestamp(jsonObject.getLong("deployTimestamp"));
        contract.setContractCreatorAddr(jsonObject.getString("contractCreatorAddr"));

        return contract;
    }
}
