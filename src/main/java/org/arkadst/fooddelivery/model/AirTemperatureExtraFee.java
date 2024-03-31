package org.arkadst.fooddelivery.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ATEF")
public class AirTemperatureExtraFee {
    @EmbeddedId
    AirTemperatureExtraFeeId airTemperatureExtraFeeId;
    Double fee;

    public AirTemperatureExtraFee(AirTemperatureExtraFeeId airTemperatureExtraFeeId, Double fee) {
        this.airTemperatureExtraFeeId = airTemperatureExtraFeeId;
        this.fee = fee;
    }

    public AirTemperatureExtraFee() {

    }

    public String getVehicle() {
        return airTemperatureExtraFeeId.getVehicle();
    }

    public Double getMinTemp() {
        return airTemperatureExtraFeeId.getMinTemp();
    }

    public Double getMaxTemp() {
        return airTemperatureExtraFeeId.getMaxTemp();
    }

    public Boolean getIncludeMin() {
        return airTemperatureExtraFeeId.getIncludeMin();
    }

    public Boolean getIncludeMax() {
        return airTemperatureExtraFeeId.getIncludeMax();
    }

    public Double getFee() {
        return fee;
    }
}
