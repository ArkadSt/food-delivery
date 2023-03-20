package org.arkadst.fooddelivery;

import jakarta.annotation.PostConstruct;
import org.arkadst.fooddelivery.model.StationId;
import org.arkadst.fooddelivery.service.FoodDeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
