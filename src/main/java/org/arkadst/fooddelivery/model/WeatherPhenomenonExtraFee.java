package org.arkadst.fooddelivery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "WPEF")
public class WeatherPhenomenonExtraFee {
    @EmbeddedId
    private WeatherPhenomenonExtraFeeId weatherPhenomenonExtraFeeId;
    private Double fee;

    public WeatherPhenomenonExtraFee(WeatherPhenomenonExtraFeeId weatherPhenomenonExtraFeeId, Double fee){
        this.weatherPhenomenonExtraFeeId = weatherPhenomenonExtraFeeId;
        this.fee = fee;
    }

    public WeatherPhenomenonExtraFee() {

    }
    public String getVehicle() {
        return weatherPhenomenonExtraFeeId.getVehicle();
    }

    public String getPhenomenon() {
        return weatherPhenomenonExtraFeeId.getPhenomenon();
    }

    public Double getFee() {
        return fee;
    }
}
