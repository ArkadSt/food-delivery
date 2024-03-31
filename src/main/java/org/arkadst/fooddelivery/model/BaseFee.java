package org.arkadst.fooddelivery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BASE_FEE")
public class BaseFee {

    @EmbeddedId
    private BaseFeeId baseFeeId;
    private Double fee;

    public BaseFee(BaseFeeId baseFeeId, Double fee){
        this.baseFeeId = baseFeeId;
        this.fee = fee;
    }

    public BaseFee() {

    }

    public String getCity() {
        return baseFeeId.getCity();
    }

    public String getVehicle() {
        return baseFeeId.getVehicle();
    }

    public Double getFee() {
        return fee;
    }
}
