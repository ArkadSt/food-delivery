package org.arkadst.fooddelivery.service;

import jakarta.annotation.PostConstruct;
import org.arkadst.fooddelivery.model.Station;
import org.arkadst.fooddelivery.model.StationId;
import org.arkadst.fooddelivery.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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

@Service
@EnableScheduling
public class FoodDeliveryService {
    @Autowired
    private final StationRepository stationRepository;
    private Long timestamp;

    public FoodDeliveryService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station get(StationId stationId) {
        return stationRepository.findById(stationId).orElse(null);
    }

    public void save(String stationName, Integer wmoCode, Double airTemp, Double windSpeed, String weatherPhenomenon, Long timestamp){
        Station station = new Station();
        station.setStationName(stationName);
        station.setAirTemp(airTemp);
        station.setWindSpeed(windSpeed);
        station.setWeatherPhenomenon(weatherPhenomenon);

        station.setStationId(new StationId(wmoCode, timestamp));

        stationRepository.save(station);
    }

    public String getFee(String city, String vehicle){

        city = city.toLowerCase();
        vehicle = vehicle.toLowerCase();
        Double fee = 0.0;

        Map<String, Map<String, Double>> rbfMap = new HashMap<>();
        Map<String, Double> tallinnMap = new HashMap<>();
        Map<String, Double> tartuMap = new HashMap<>();
        Map<String, Double> parnuMap = new HashMap<>();

// Populate the nested HashMaps
        tallinnMap.put("car", 4.0);
        tallinnMap.put("scooter", 3.5);
        tallinnMap.put("bike", 3.0);
        tartuMap.put("car", 3.5);
        tartuMap.put("scooter", 3.0);
        tartuMap.put("bike", 2.5);
        parnuMap.put("car", 3.0);
        parnuMap.put("scooter", 2.5);
        parnuMap.put("bike", 2.0);

// Put the nested HashMaps into the parent HashMap
        rbfMap.put("tallinn", tallinnMap);
        rbfMap.put("tartu", tartuMap);
        rbfMap.put("pärnu", parnuMap);

        fee += rbfMap.get(city).get(vehicle);

        Map<String, StationId> weatherStation = new HashMap<>();
        weatherStation.put("tallinn", new StationId(26038, timestamp));
        weatherStation.put("tartu", new StationId(26242, timestamp));
        weatherStation.put("pärnu", new StationId(41803, timestamp));

        double temperature = get(weatherStation.get(city)).getAirTemp();
        double windSpeed = get(weatherStation.get(city)).getWindSpeed();
        String weatherPhenomenon = get(weatherStation.get(city)).getWeatherPhenomenon().toLowerCase();

        if (vehicle.equals("scooter") || vehicle.equals("bike")){
            if (temperature <= 0 && temperature >= -10){
                fee += 0.5;
            } else if (temperature < -10){
                fee += 1;
            }

            if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("sleet")){
                fee += 1;
            }
            if (weatherPhenomenon.contains("rain")){
                fee += 0.5;
            }
            if (weatherPhenomenon.contains("glaze") || weatherPhenomenon.contains("hail") || weatherPhenomenon.contains("thunder")){
                return "Usage of selected vehicle type is forbidden";
            }
        }

        if (vehicle.equals("bike")){
            if (windSpeed >= 10 && windSpeed <= 20){
                fee += 0.5;
            } else if (windSpeed > 20){
                return "Usage of selected vehicle type is forbidden";
            }
        }


        return fee + " EUR";
    }
    @PostConstruct
    @Scheduled(cron = "${cron.expression}")
    public void pullFromDatabase(){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
            doc.getDocumentElement().normalize();
            timestamp = Long.parseLong(doc.getElementsByTagName("observations").item(0).getAttributes().item(0).getNodeValue());
            NodeList allNodeList = doc.getElementsByTagName("station");

            System.out.println(timestamp);
            loop0:
            for (int i = 0; i < allNodeList.getLength(); i++) {
                Node stationNode = allNodeList.item(i);
                NodeList nodeList = stationNode.getChildNodes();

                String stationName = null, weatherPhenomenon = null;
                Integer wmoCode = null;
                Double airTemp = null, windSpeed = null;

                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);
                    String name = node.getNodeName();
                    String value = node.getTextContent();

                    try {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            switch (name) {
                                case "name" -> {
                                    if (!value.equals("Tallinn-Harku") && !value.equals("Tartu-Tõravere") && !value.equals("Pärnu"))
                                        continue loop0;
                                    stationName = value;
                                }
                                case "wmocode" -> wmoCode = Integer.parseInt(value);
                                case "airtemperature" -> airTemp = Double.parseDouble(value);
                                case "windspeed" -> windSpeed = Double.parseDouble(value);
                                case "phenomenon" -> weatherPhenomenon = value;
                                default -> {
                                    continue;
                                }
                            }
                            System.out.println(name + " " + value);
                        }

                    } catch (NumberFormatException ignore){}

                }


                save(stationName, wmoCode, airTemp, windSpeed, weatherPhenomenon, timestamp);

            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
    }

}
