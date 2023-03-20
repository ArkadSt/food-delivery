package org.arkadst.fooddelivery.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class StationId implements Serializable {
    private Integer wmoCode;
    private Long timestamp;

    public StationId(){}

    public StationId(Integer wmoCode, Long timestamp){
        this.wmoCode = wmoCode;
        this.timestamp = timestamp;
    }
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getWmoCode() {
        return wmoCode;
    }

    public void setWmoCode(Integer wmoCode) {
        this.wmoCode = wmoCode;
    }
}
