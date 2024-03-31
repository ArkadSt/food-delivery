package org.arkadst.fooddelivery.service;

import jakarta.annotation.PostConstruct;
import org.arkadst.fooddelivery.model.*;
import org.arkadst.fooddelivery.repository.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@EnableScheduling
public class FoodDeliveryService {

    private final StationRepository stationRepository;
    private final BaseFeeRepository baseFeeRepository;
    private final AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository;
    private final WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository;
    private final WindSpeedExtraFeeRepository windSpeedExtraFeeRepository;
    private final int TALLINN_WMO_CODE = 26038;
    private final int TARTU_WMO_CODE = 26242;
    private final int PÄRNU_WMO_CODE = 41803;
    private long current_timestamp;
    private final Map<String, Integer> CITY_WMO_CODE_MAP;

    public FoodDeliveryService(StationRepository stationRepository, BaseFeeRepository baseFeeRepository, AirTemperatureExtraFeeRepository airTemperatureExtraFeeRepository, WeatherPhenomenonExtraFeeRepository weatherPhenomenonExtraFeeRepository, WindSpeedExtraFeeRepository windSpeedExtraFeeRepository) {
        this.stationRepository = stationRepository;
        this.baseFeeRepository = baseFeeRepository;
        this.airTemperatureExtraFeeRepository = airTemperatureExtraFeeRepository;
        this.weatherPhenomenonExtraFeeRepository = weatherPhenomenonExtraFeeRepository;
        this.windSpeedExtraFeeRepository = windSpeedExtraFeeRepository;

        CITY_WMO_CODE_MAP = new HashMap<>();
        CITY_WMO_CODE_MAP.put("tallinn", TALLINN_WMO_CODE);
        CITY_WMO_CODE_MAP.put("tartu", TARTU_WMO_CODE);
        CITY_WMO_CODE_MAP.put("pärnu", PÄRNU_WMO_CODE);

    }

    /**
     * Pulls data from the database based on the StationId candidate key
     * @param stationId candidate key
     * @return Station database entry
     */
    public Station getStation(StationId stationId) {
        return stationRepository.findById(stationId).orElse(null);
    }




    /**
     * Calculates delivery fee which is return
     * @param city city name
     * @param vehicle vehicle type
     * @param timestamp Epoch timestamp
     * @return delivery fee in the form X.XX EUR or an error message
     */
    public String getFee(String city, String vehicle, Long timestamp){

        city = city.toLowerCase();
        vehicle = vehicle.toLowerCase();
        Double fee = null;

        for (BaseFee baseFee : baseFeeRepository.findAll()){
            if (baseFee.getCity().equals(city) && baseFee.getVehicle().equals(vehicle)){
                fee = baseFee.getFee();
            }
        }

        if (fee == null){
            return "Wrong city or vehicle type";
        }


        Station station = getStation(new StationId(CITY_WMO_CODE_MAP.get(city), timestamp));
        if (station == null){
            return "No database entry associated with this timestamp";
        }



        double airTemperature = station.getAirTemp();
        double windSpeed = station.getWindSpeed();
        String weatherPhenomenon = station.getWeatherPhenomenon().toLowerCase();

        for (AirTemperatureExtraFee airTemperatureExtraFee : airTemperatureExtraFeeRepository.findAll()){
            if (airTemperatureExtraFee.getVehicle().equals(vehicle)){
                if (airTemperatureExtraFee.getIncludeMin() && airTemperatureExtraFee.getIncludeMax()
                        && airTemperatureExtraFee.getMinTemp() <= airTemperature && airTemperature <= airTemperatureExtraFee.getMaxTemp()
                        || !airTemperatureExtraFee.getIncludeMin() && airTemperatureExtraFee.getIncludeMax()
                        && airTemperatureExtraFee.getMinTemp() < airTemperature && airTemperature <= airTemperatureExtraFee.getMaxTemp()
                        || airTemperatureExtraFee.getIncludeMin() && !airTemperatureExtraFee.getIncludeMax()
                        && airTemperatureExtraFee.getMinTemp() <= airTemperature && airTemperature < airTemperatureExtraFee.getMaxTemp()
                        || !airTemperatureExtraFee.getIncludeMin() && !airTemperatureExtraFee.getIncludeMax()
                        && airTemperatureExtraFee.getMinTemp() < airTemperature && airTemperature < airTemperatureExtraFee.getMaxTemp()){
                    if (airTemperatureExtraFee.getFee() < 0){
                        return "Usage of selected vehicle type is forbidden";
                    } else {
                        fee += airTemperatureExtraFee.getFee();
                    }
                }
            }
        }

        for (WindSpeedExtraFee windSpeedExtraFee : windSpeedExtraFeeRepository.findAll()){
            if (windSpeedExtraFee.getVehicle().equals(vehicle)){
                if (windSpeedExtraFee.getIncludeMin() && windSpeedExtraFee.getIncludeMax()
                && windSpeedExtraFee.getMinWindSpeed() <= windSpeed && windSpeed <= windSpeedExtraFee.getMaxWindSpeed()
                || !windSpeedExtraFee.getIncludeMin() && windSpeedExtraFee.getIncludeMax()
                && windSpeedExtraFee.getMinWindSpeed() < windSpeed && windSpeed <= windSpeedExtraFee.getMaxWindSpeed()
                || windSpeedExtraFee.getIncludeMin() && !windSpeedExtraFee.getIncludeMax()
                && windSpeedExtraFee.getMinWindSpeed() <= windSpeed && windSpeed < windSpeedExtraFee.getMaxWindSpeed()
                || !windSpeedExtraFee.getIncludeMin() && !windSpeedExtraFee.getIncludeMax()
                && windSpeedExtraFee.getMinWindSpeed() < windSpeed && windSpeed < windSpeedExtraFee.getMaxWindSpeed()){
                    if (windSpeedExtraFee.getFee() < 0){
                        return "Usage of selected vehicle type is forbidden";
                    } else {
                        fee += windSpeedExtraFee.getFee();
                    }
                }
            }
        }

        for (WeatherPhenomenonExtraFee weatherPhenomenonExtraFee : weatherPhenomenonExtraFeeRepository.findAll()){
            if (vehicle.equals(weatherPhenomenonExtraFee.getVehicle())){
                if (weatherPhenomenon.contains(weatherPhenomenonExtraFee.getPhenomenon())){
                    if (weatherPhenomenonExtraFee.getFee() < 0){
                        return "Usage of selected vehicle type is forbidden";
                    } else {
                        fee += weatherPhenomenonExtraFee.getFee();
                    }
                }
            }

        }

        return fee + " EUR";
    }

    /**
     * Runs getFee using the latest recorded timestamp
     * @param city city
     * @param vehicle vehicle
     * @return fee or error
     */
    public String getFee(String city, String vehicle){
        return getFee(city, vehicle, current_timestamp);
    }

    /**
     * Takes datetime of type LocalDateTime, converts it to Epoch time and then runs getFee
     * @param city vity
     * @param vehicle vehicle
     * @param dateTime date and time
     * @return fee or error
     */
    public String getFee(String city, String vehicle, LocalDateTime dateTime){
        return getFee(city, vehicle, dateTime.atZone(ZoneId.of("Europe/Tallinn")).toEpochSecond());
    }

    // The following methods write new entries to the database
    public Station saveStation(String stationName, Integer wmoCode, Double airTemp, Double windSpeed, String weatherPhenomenon, Long timestamp){
        return stationRepository.save(new Station(new StationId(wmoCode, timestamp), stationName, airTemp, windSpeed, weatherPhenomenon));
    }
    
    public BaseFee saveBaseFee(String city, String vehicle, Double fee){
        return baseFeeRepository.save(new BaseFee(new BaseFeeId(city, vehicle), fee));
    }

    public AirTemperatureExtraFee saveAirTemperatureExtraFee(String vehicle, double min_temp, double max_temp, Boolean include_min, Boolean include_max, double fee){
        return airTemperatureExtraFeeRepository.save(new AirTemperatureExtraFee(new AirTemperatureExtraFeeId(vehicle, min_temp, max_temp, include_min, include_max), fee));
    }

    public WindSpeedExtraFee saveWindSpeedExtraFee(String vehicle, double min_wind_speed, double max_win_speed, Boolean include_min, Boolean include_max, double fee){
        return windSpeedExtraFeeRepository.save(new WindSpeedExtraFee(new WindSpeedExtraFeeId(vehicle, min_wind_speed, max_win_speed, include_min, include_max), fee));
    }

    public WeatherPhenomenonExtraFee saveWeatherPhenomenonExtraFee(String vehicle, String phenomenon, double fee){
        return weatherPhenomenonExtraFeeRepository.save(new WeatherPhenomenonExtraFee(new WeatherPhenomenonExtraFeeId(vehicle, phenomenon), fee));
    }

    // The following methods delete entries from the database
    public void deleteBaseFee(String city, String vehicle){
        baseFeeRepository.deleteById(new BaseFeeId(city, vehicle));
    }

    public void deleteAirTemperatureExtraFee(String vehicle, double min_temp, double max_temp, Boolean include_min, Boolean include_max){
        airTemperatureExtraFeeRepository.deleteById(new AirTemperatureExtraFeeId(vehicle, min_temp, max_temp, include_min, include_max));
    }

    public void deleteWindSpeedExtraFee(String vehicle, double min_wind_speed, double max_win_speed, Boolean include_min, Boolean include_max){
        windSpeedExtraFeeRepository.deleteById(new WindSpeedExtraFeeId(vehicle, min_wind_speed, max_win_speed, include_min, include_max));
    }

    public void deleteWeatherPhenomenonExtraFee(String vehicle, String phenomenon){
        weatherPhenomenonExtraFeeRepository.deleteById(new WeatherPhenomenonExtraFeeId(vehicle, phenomenon));
    }

    /**
     * Populates empty tables with default business rules, makes first invocation of getUpdatedWeatherInfo()
     */
    @PostConstruct
    public void populateDatabase(){
        if (baseFeeRepository.count() == 0){
            saveBaseFee("Tallinn", "Car", 4.0);
            saveBaseFee("Tallinn", "Scooter", 3.5);
            saveBaseFee("Tallinn", "Bike", 3.0);
            saveBaseFee("Tartu", "Car", 3.5);
            saveBaseFee("Tartu", "Scooter", 3.0);
            saveBaseFee("Tartu", "Bike", 2.5);
            saveBaseFee("Pärnu", "Car", 3.0);
            saveBaseFee("Pärnu", "Scooter", 2.5);
            saveBaseFee("Pärnu", "Bike", 2.0);
        }

        if (airTemperatureExtraFeeRepository.count() == 0){
            for (String vehicle : new String[]{"Scooter", "Bike"}) {
                saveAirTemperatureExtraFee(vehicle, Double.NEGATIVE_INFINITY, -10, false, false, 1);
                saveAirTemperatureExtraFee(vehicle, -10, 0, true, true, 0.5);
            }

        }

        if (windSpeedExtraFeeRepository.count() == 0){
            saveWindSpeedExtraFee("Bike", 10, 20, true, true, 0.5);
            saveWindSpeedExtraFee("Bike", 20, Double.POSITIVE_INFINITY, false, false, -1.0);
        }

        if (weatherPhenomenonExtraFeeRepository.count() == 0){
            for (String vehicle : new String[]{"Scooter", "Bike"}){
                saveWeatherPhenomenonExtraFee(vehicle, "snow", 1.0);
                saveWeatherPhenomenonExtraFee(vehicle, "sleet", 1.0);
                saveWeatherPhenomenonExtraFee(vehicle, "rain", 0.5);
                saveWeatherPhenomenonExtraFee(vehicle, "glaze", -1.0);
                saveWeatherPhenomenonExtraFee(vehicle, "hail", -1.0);
                saveWeatherPhenomenonExtraFee(vehicle, "thunder", -1.0);
            }
        }

        getUpdatedWeatherInfo();
    }

    /**
     * Pulls XML, extracts relevant data and saves it to the STATION table of our database
     */
    @Scheduled(cron = "${cron.expression}")
    public void getUpdatedWeatherInfo() {
        var tuple = XMLParser.parse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php", Arrays.asList(TALLINN_WMO_CODE, TARTU_WMO_CODE, PÄRNU_WMO_CODE));
        current_timestamp = tuple.first();
        var stations = tuple.second();

        for (Map<String, String> station : stations) {
            String stationName = station.get("name");
            int wmoCode = Integer.parseInt(station.get("wmocode"));
            double airTemp = Double.parseDouble(station.get("airtemperature"));
            double windSpeed = Double.parseDouble(station.get("windspeed"));
            String weatherPhenomenon = station.get("phenomenon");

            saveStation(stationName, wmoCode, airTemp, windSpeed, weatherPhenomenon, current_timestamp);
        }

    }

}
