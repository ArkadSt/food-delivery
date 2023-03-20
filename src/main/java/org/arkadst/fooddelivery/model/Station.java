package org.arkadst.fooddelivery.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "STATIONS")
public class Station {

    @EmbeddedId
    private StationId stationId;

    private String stationName;

    private Double airTemp;

    private Double windSpeed;

    private String weatherPhenomenon;



    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(Double airTemp) {
        this.airTemp = airTemp;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public StationId getStationId() {
        return stationId;
    }

    public void setStationId(StationId stationId) {
        this.stationId = stationId;
    }
}

