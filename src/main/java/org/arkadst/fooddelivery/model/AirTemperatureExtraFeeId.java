package org.arkadst.fooddelivery.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AirTemperatureExtraFeeId implements Serializable {

    String vehicle;
    Double minTemp;
    Double maxTemp;

    Boolean includeMin;
    Boolean includeMax;

    public AirTemperatureExtraFeeId(String vehicle, Double minTemp, Double maxTemp, Boolean includeMin, Boolean includeMax) {
        this.vehicle = vehicle.toLowerCase();
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.includeMin = includeMin;
        this.includeMax = includeMax;
    }

    public AirTemperatureExtraFeeId() {

    }

    public String getVehicle() {
        return vehicle;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public Boolean getIncludeMin() {
        return includeMin;
    }

    public Boolean getIncludeMax() {
        return includeMax;
    }
}
