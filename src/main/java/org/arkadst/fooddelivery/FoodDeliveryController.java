package org.arkadst.fooddelivery;

import org.arkadst.fooddelivery.service.FoodDeliveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FoodDeliveryController {

    private final FoodDeliveryService foodDeliveryService;

    public FoodDeliveryController(FoodDeliveryService foodDeliveryService) {
        this.foodDeliveryService = foodDeliveryService;
    }

    @GetMapping("/get-fee/{city}/{vehicle}")
    public String getFee(@PathVariable String city, @PathVariable String vehicle){
        return foodDeliveryService.getFee(city, vehicle);
    }

}
