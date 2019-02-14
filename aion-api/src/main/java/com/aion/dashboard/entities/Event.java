package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Event")
public class Event {
    @Id
    private Long eventId;
    private Long blockNumber;
    private Long transactionId;
    private Long eventTimestamp;
    private String contractAddr;
    private String parameterList;
    private String inputList;
    private String name;

    public Long getEventId()         { return eventId;        }
    public Long getBlockNumber()     { return blockNumber;    }
    public Long getEventTimestamp()  { return eventTimestamp; }
    public Long getTransactionId()   { return transactionId;  }
    public String getParameterList() { return parameterList;  }
    public String getContractAddr()  { return contractAddr;   }
    public String getInputList()     { return inputList;      }
    public String getName()          { return name;           }

    public void setEventId(Long eventId)               { this.eventId        = eventId;        }
    public void setBlockNumber(Long blockNumber)       { this.blockNumber    = blockNumber;    }
    public void setContractAddr(String contractAddr)   { this.contractAddr   = contractAddr;   }
    public void setTransactionId(Long transactionId)   { this.transactionId  = transactionId;  }
    public void setEventTimestamp(Long eventTimestamp) { this.eventTimestamp = eventTimestamp; }
    public void setParameterList(String parameterList) { this.parameterList  = parameterList;  }
    public void setInputList(String inputList)         { this.inputList      = inputList;      }
    public void setName(String name)                   { this.name           = name;           }

    public static Event toObject(JSONObject jsonObject) {
        Event event = new Event();

        event.setName(jsonObject.getString("name"));
        event.setEventId(jsonObject.getLong("eventId"));
        event.setInputList(jsonObject.getString("inputList"));
        event.setContractAddr(jsonObject.getString("contractAddr"));
        event.setTransactionId(jsonObject.getLong("transactionId"));
        event.setEventTimestamp(jsonObject.getLong("eventTimestamp"));
        event.setParameterList(jsonObject.getString("parameterList"));

        return event;
    }
}

