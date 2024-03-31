package org.arkadst.fooddelivery.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Embeddable
public class WeatherPhenomenonExtraFeeId implements Serializable {

    private String vehicle;
    //@Column(name="phenomenon", nullable = false)
    private String phenomenon;
    //@Column(name="fee", nullable = false)

    public WeatherPhenomenonExtraFeeId(){

    }

    public WeatherPhenomenonExtraFeeId(String vehicle, String phenomenon){
        this.vehicle = vehicle.toLowerCase();
        this.phenomenon = phenomenon.toLowerCase();
    }


    public String getVehicle() {
        return vehicle;
    }

    public String getPhenomenon() {
        return phenomenon;
    }
}
