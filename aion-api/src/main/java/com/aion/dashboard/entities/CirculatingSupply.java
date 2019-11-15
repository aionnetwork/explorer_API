package com.aion.dashboard.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="circulating_supply")
public class CirculatingSupply {

    @Id
    private Timestamp startDate;
    private BigDecimal supply;

    public CirculatingSupply() {
    }

    public CirculatingSupply(Timestamp startDate, BigDecimal supply) {
        this.startDate = startDate;
        this.supply = supply;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public BigDecimal getSupply() {
        return supply;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setSupply(BigDecimal supply) {
        this.supply = supply;
    }
}
