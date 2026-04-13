package com.tripPortal.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class Transport {
    String name;
    String TransportID;
    ArrayList<Section> sections;
    boolean available;

    public Transport(String name) {
        this.name = name;
        this.TransportID = randomGenerateID();
        this.available = true;
        this.sections = new ArrayList<>();
    }

    public Transport(String id, String placeholder) {
        this.TransportID = id;
    }

    private String randomGenerateID() {
        String id = "";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int number = (rand.nextInt(9));
            id += number;
        }

        boolean isUnique = false;
        
        while (!isUnique) {
            id = "";
            for (int i = 0; i < 6; i++) {
                int number = (rand.nextInt(9));
                id += number;
            }
            
            isUnique = isTransportIDUnique(id);
        }

        return id;
    }
    
    private boolean isTransportIDUnique(String transportId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("src/Database/Transport.json");
            if (!file.exists()) {
                return true;
            }
            ArrayNode transports = (ArrayNode) mapper.readTree(file);
            for (JsonNode node : transports) {
                if (node.has("transportID") && node.get("transportID").asText().equals(transportId)) {
                    return false;
                }
            }
            return true;
        } catch (IOException ex) {
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public String getTransportID() {
        return TransportID;
    }

    public void setTransportID(String transportID) {
        TransportID = transportID;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public static Transport fromJson(String transportID, JsonNode root) {
        for (JsonNode node : root) {
            if (node.get("transportID").asText().equals(transportID)) {
                String type = node.get("type").asText();
                return switch (type) {
                    case "Plane" -> new Plane(node);
                    case "Boat" -> new Boat(node);
                    case "Train" -> new Train(node);
                    default -> throw new IllegalArgumentException("Unknown transport type: " + type);
                };
            }
        }
        throw new IllegalArgumentException("Transport not found: " + transportID);
    }

    public void delete(String transportId) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            File transportFile = new File("src/Database/Transport.json");
            ArrayNode transports = (ArrayNode) mapper.readTree(transportFile);
            for (int i = 0; i < transports.size(); i++) {
                if (transportId.equals(transports.get(i).path("transportID").asText())) {
                    transports.remove(i);
                    break;
                }
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(transportFile, transports);

            File tripFile = new File("src/Database/Trip.json");
            ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
            ArrayNode removedTripIds = mapper.createArrayNode();
            for (int i = trips.size() - 1; i >= 0; i--) {
                if (transportId.equals(trips.get(i).path("transport").asText())) {
                    removedTripIds.add(trips.get(i).path("id").asText());
                    trips.remove(i);
                }
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);

            if (removedTripIds.size() > 0) {
                File companyFile = new File("src/Database/Company.json");
                ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
                for (JsonNode company : companies) {
                    if (!company.has("Trips")) {
                        continue;
                    }
                    ArrayNode companyTrips = (ArrayNode) company.get("Trips");
                    for (int i = companyTrips.size() - 1; i >= 0; i--) {
                        String tripId = companyTrips.get(i).asText();
                        for (JsonNode removedId : removedTripIds) {
                            if (removedId.asText().equals(tripId)) {
                                companyTrips.remove(i);
                                break;
                            }
                        }
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Unable to delete transport.");
            return;
        }
    }
}
