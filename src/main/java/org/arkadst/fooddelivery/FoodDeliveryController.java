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

    @GetMapping("/get-fee/{city}/{vehicle}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle){
        return foodDeliveryService.getFee(city, vehicle) + '\n';
    }

    @GetMapping("/get-fee/{city}/{vehicle}/timestamp/{timestamp}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle, @PathVariable Long timestamp){
        return foodDeliveryService.getFee(city, vehicle, timestamp) + '\n';
    }

    @GetMapping("/get-fee/{city}/{vehicle}/datetime/{datetime}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle, @PathVariable LocalDateTime datetime){
        return foodDeliveryService.getFee(city, vehicle, datetime) + '\n';
    }

    /*@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public String handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return "You have entered the wrong type ";
    }*/

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


}
