package org.arkadst.fooddelivery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "WSEF")
public class WindSpeedExtraFee {
    @EmbeddedId
    WindSpeedExtraFeeId windSpeedExtraFeeId;
    Double fee;

    public WindSpeedExtraFee(WindSpeedExtraFeeId windSpeedExtraFeeId, Double fee) {
        this.windSpeedExtraFeeId = windSpeedExtraFeeId;
        this.fee = fee;
    }

    public WindSpeedExtraFee() {

    }
    public String getVehicle() {
        return windSpeedExtraFeeId.getVehicle();
    }

    public Double getMinWindSpeed() {
        return windSpeedExtraFeeId.getMinWindSpeed();
    }

    public Double getMaxWindSpeed() {
        return windSpeedExtraFeeId.getMaxWindSpeed();
    }

    public Boolean getIncludeMin() {
        return windSpeedExtraFeeId.getIncludeMin();
    }

    public Boolean getIncludeMax() {
        return windSpeedExtraFeeId.getIncludeMax();
    }

    public Double getFee() {
        return fee;
    }
}
