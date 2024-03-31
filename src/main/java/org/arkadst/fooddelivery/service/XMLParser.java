package org.arkadst.fooddelivery.service;

import org.arkadst.fooddelivery.datatypes.Tuple;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XMLParser {
    /**
     * Parses given XML file given the URL and filter and returns the tuple of the latest timestamp and list of hashmaps corresponding to weather stations
     * @param url URL of XML file
     * @param filter extract only weather stations with the following WMO codes
     * @return the tuple of the latest timestamp and list of hashmaps corresponding to weather stations
     */
    public static Tuple<Long, List<Map<String, String>>> parse(String url, List<Integer> filter){

        List<Map<String, String>> stations = new ArrayList<>();
        long current_timestamp = 0;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            current_timestamp = Long.parseLong(doc.getElementsByTagName("observations").item(0).getAttributes().item(0).getNodeValue());


            NodeList allNodeList = doc.getElementsByTagName("station");

            // Process each station
            for (int i = 0; i < allNodeList.getLength(); i++) {
                Node stationNode = allNodeList.item(i);
                NodeList nodeList = stationNode.getChildNodes();

                Map<String, String> nodes = new HashMap<>();

                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);
                    String name = node.getNodeName();
                    String value = node.getTextContent();
                    nodes.put(name, value);
                }

                try {
                    int wmoCode = Integer.parseInt(nodes.get("wmocode"));
                    if (filter.contains(wmoCode)) {
                        stations.add(nodes);
                    }
                } catch (NumberFormatException ignore){}
            }


        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.println(e.getMessage());
        }

        return new Tuple<>(current_timestamp, stations);
    }

}
