package org.arkadst.fooddelivery;

import org.arkadst.fooddelivery.model.*;
import org.arkadst.fooddelivery.repository.AirTemperatureExtraFeeRepository;
import org.arkadst.fooddelivery.repository.BaseFeeRepository;
import org.arkadst.fooddelivery.repository.WeatherPhenomenonExtraFeeRepository;
import org.arkadst.fooddelivery.repository.WindSpeedExtraFeeRepository;
import org.arkadst.fooddelivery.service.FoodDeliveryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;


@RestController
public class FoodDeliveryController {

    private final FoodDeliveryService foodDeliveryService;

    public FoodDeliveryController(FoodDeliveryService foodDeliveryService) {
        this.foodDeliveryService = foodDeliveryService;
    }

    // The following methods receive and manipulate data using web requests

    /**
     * Get fee based on latest weather data
     * @param city city
     * @param vehicle vehicle type
     * @return fee or error
     */
    @GetMapping("/get-fee/{city}/{vehicle}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle){
        return foodDeliveryService.getFee(city, vehicle) + '\n';
    }

    /**
     * Get fee based on weather data at a specific time
     * @param city city
     * @param vehicle vehicle type
     * @param timestamp Epoch timestamp
     * @return fee or error
     */
    @GetMapping("/get-fee/{city}/{vehicle}/timestamp/{timestamp}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle, @PathVariable Long timestamp){
        return foodDeliveryService.getFee(city, vehicle, timestamp) + '\n';
    }

    /**
     * Get fee based on weather data at a specific time
     * @param city city
     * @param vehicle vehicle type
     * @param datetime date and time in ISO-8601 format, such as 2007-12-03T10:15:30
     * @return fee or error
     */
    @GetMapping("/get-fee/{city}/{vehicle}/datetime/{datetime}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle, @PathVariable LocalDateTime datetime){
        return foodDeliveryService.getFee(city, vehicle, datetime) + '\n';
    }

    // the following methods submit new / update existing business rules
    @PostMapping("/base-fee")
    public BaseFee newBaseFee(@RequestParam String city, @RequestParam String vehicle, @RequestParam Double fee){
        return foodDeliveryService.saveBaseFee(city, vehicle, fee);
    }

    @PostMapping("/atef")
    public AirTemperatureExtraFee newAirTemperatureExtraFee(@RequestParam String vehicle, @RequestParam double min_temp, @RequestParam double max_temp, @RequestParam Boolean include_min, @RequestParam Boolean include_max, @RequestParam double fee){
        return foodDeliveryService.saveAirTemperatureExtraFee(vehicle, min_temp, max_temp, include_min, include_max, fee);
    }

    @PostMapping("/wsef")
    public WindSpeedExtraFee newWindSpeedExtraFee(@RequestParam String vehicle, @RequestParam double min_wind_speed, @RequestParam double max_win_speed, @RequestParam Boolean include_min, @RequestParam Boolean include_max, @RequestParam double fee){
        return foodDeliveryService.saveWindSpeedExtraFee(vehicle, min_wind_speed, max_win_speed, include_min, include_max, fee);
    }

    @PostMapping("/wpef")
    public WeatherPhenomenonExtraFee newWeatherPhenomenonExtraFee(@RequestParam String vehicle, @RequestParam String phenomenon, @RequestParam double fee){
        return foodDeliveryService.saveWeatherPhenomenonExtraFee(vehicle, phenomenon, fee);
    }

    // the following methods delete business rules
    @DeleteMapping("/delete-base-fee/{city}/{vehicle}")
    public void deleteBaseFee(@PathVariable String city, @PathVariable String vehicle){
        foodDeliveryService.deleteBaseFee(city, vehicle);
    }

    @DeleteMapping("/delete-atef/{vehicle}/{min_temp}/{max_temp}/{include_min}/{include_max}")
    public void deleteAirTemperatureExtraFee(@PathVariable String vehicle, @PathVariable double min_temp, @PathVariable double max_temp, @PathVariable Boolean include_min, @PathVariable Boolean include_max){
        foodDeliveryService.deleteAirTemperatureExtraFee(vehicle, min_temp, max_temp, include_min, include_max);
    }

    @DeleteMapping("/delete-wsef/{vehicle}/{min_wind_speed}/{max_win_speed}/{include_min}/{include_max}")
    public void deleteWindSpeedExtraFee(@PathVariable String vehicle, @PathVariable double min_wind_speed, @PathVariable double max_win_speed, @PathVariable Boolean include_min, @PathVariable Boolean include_max){
        foodDeliveryService.deleteWindSpeedExtraFee(vehicle, min_wind_speed, max_win_speed, include_min, include_max);
    }

    @DeleteMapping("/delete-wpef/{vehicle}/{phenomenon}")
    public void deleteWeatherPhenomenonExtraFee(@PathVariable String vehicle, @PathVariable String phenomenon){
        foodDeliveryService.deleteWeatherPhenomenonExtraFee(vehicle, phenomenon);
    }

    // nicer error message on wrong type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public String handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return "You have entered the wrong type somewhere. Check your request";
    }

}
