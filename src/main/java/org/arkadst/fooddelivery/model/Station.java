package org.arkadst.fooddelivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Locale;

@Entity
@Table(name = "STATIONS")
public class Station {

    @EmbeddedId
    private StationId stationId;
    //@Column(name="station_name")
    private String stationName;
    //@Column(name="air_temp", nullable = false)
    private Double airTemp;

    //@Column(name="wind_speed", nullable = false)
    private Double windSpeed;

    //@Column(name="weather_phenomenon")
    private String weatherPhenomenon;

    public Station(StationId stationId, String stationName, Double airTemp, Double windSpeed, String weatherPhenomenon) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon.toLowerCase();
    }

    public Station() {

    }

    public Double getAirTemp() {
        return airTemp;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

}


