package org.arkadst.fooddelivery.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BaseFeeId implements Serializable {
    private String city;
    private String vehicle;

    public BaseFeeId(String city, String vehicle){
        this.city = city.toLowerCase();
        this.vehicle = vehicle.toLowerCase();
    }

    public BaseFeeId() {

    }


    public String getCity() {
        return city;
    }

    public String getVehicle() {
        return vehicle;
    }
}
