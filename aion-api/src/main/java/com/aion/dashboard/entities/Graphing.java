package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Graphing")
public class Graphing {

    @Id
    private Long id;
    private BigDecimal value;
    private Integer date;
    private Integer year;
    private Integer month;
    private String detail;
    private String graphType;
    private Long timestamp;
    private Long blockNumber;

    public Long getId()          { return id;          }
    public BigDecimal getValue() { return value;       }
    public Integer getDate()     { return date;        }
    public Integer getYear()     { return year;        }
    public Integer getMonth()    { return month;       }
    public String getDetail()    { return detail;      }
    public String getGraphType() { return graphType;   }
    public Long getTimestamp()   { return timestamp;   }
    public Long getBlockNumber() { return blockNumber; }

    public void setId(Long id)                   { this.id          = id;          }
    public void setValue(BigDecimal value)       { this.value       = value;       }
    public void setDate(Integer date)            { this.date        = date;        }
    public void setYear(Integer year)            { this.year        = year;        }
    public void setMonth(Integer month)          { this.month       = month;       }
    public void setDetail(String detail)         { this.detail      = detail;      }
    public void setGraphType(String graphType)   { this.graphType   = graphType;   }
    public void setTimestamp(Long timestamp)     { this.timestamp   = timestamp;   }
    public void setBlockNumber(Long blockNumber) { this.blockNumber = blockNumber; }

    public static Graphing toObject(JSONObject jsonObject) {
        Graphing graphing = new Graphing();

        graphing.setId(jsonObject.getLong("id"));
        graphing.setDate(jsonObject.getInt("date"));
        graphing.setYear(jsonObject.getInt("year"));
        graphing.setMonth(jsonObject.getInt("month"));
        graphing.setDetail(jsonObject.getString("detail"));
        graphing.setValue(new BigDecimal(jsonObject.getLong("value")));
        graphing.setTimestamp(jsonObject.getLong("timestamp"));
        graphing.setGraphType(jsonObject.getString("graphType"));
        graphing.setBlockNumber(jsonObject.getLong("blockNumber"));

        return graphing;
    }
}
