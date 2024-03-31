package org.arkadst.fooddelivery.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class WindSpeedExtraFeeId implements Serializable {

    String vehicle;
    //@Column(name = "min_wind_speed", nullable = false)
    Double minWindSpeed;
    //@Column(name = "max_wind_speed", nullable = false)
    Double maxWindSpeed;
    //@Column(name="fee", nullable = false)
    Boolean includeMin;
    Boolean includeMax;

    public WindSpeedExtraFeeId(String vehicle, Double minWindSpeed, Double maxWindSpeed, Boolean includeMin, Boolean includeMax) {
        this.vehicle = vehicle.toLowerCase();
        this.minWindSpeed = minWindSpeed;
        this.maxWindSpeed = maxWindSpeed;
        this.includeMin = includeMin;
        this.includeMax = includeMax;
    }

    public WindSpeedExtraFeeId() {

    }

    public String getVehicle() {
        return vehicle;
    }

    public Double getMinWindSpeed() {
        return minWindSpeed;
    }

    public Double getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public Boolean getIncludeMin() {
        return includeMin;
    }

    public Boolean getIncludeMax() {
        return includeMax;
    }
}
