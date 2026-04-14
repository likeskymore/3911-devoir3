package com.tripPortal.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class TransportPrototype {
    String name;
    String TransportID;
    ArrayList<Section> sections;
    boolean available;

    public TransportPrototype(String name) {
        this.name = name;
        this.TransportID = randomGenerateID();
        this.available = true;
        this.sections = new ArrayList<>();
    }

    public TransportPrototype(String id, String placeholder) {
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
            if (!file.exists()) return true;

            JsonNode root = mapper.readTree(file);

            ArrayNode transports;
            if (root.isArray()) {
                transports = (ArrayNode) root;
            } else if (root.has("transports") && root.get("transports").isArray()) {
                transports = (ArrayNode) root.get("transports");
            } else {
                return true;
            }

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

    public static TransportPrototype fromJson(String transportID, JsonNode root) {
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

            // Handle Transport.json
            File transportFile = new File("src/Database/Transport.json");
            JsonNode transportRoot = mapper.readTree(transportFile);
            ArrayNode transports;
            if (transportRoot.isArray()) {
                transports = (ArrayNode) transportRoot;
            } else if (transportRoot.has("transports") && transportRoot.get("transports").isArray()) {
                transports = (ArrayNode) transportRoot.get("transports");
            } else {
                System.err.println("Unable to delete transport.");
                return;
            }

            ObjectNode snapShot = mapper.createObjectNode();
            for (int i = 0; i < transports.size(); i++) {
                if (transportId.equals(transports.get(i).path("transportID").asText())) {
                    snapShot.set("transport", transports.get(i));
                    transports.remove(i);
                    break;
                }
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(transportFile, transports);

            // Handle Trip.json
            File tripFile = new File("src/Database/Trip.json");
            JsonNode tripRoot = mapper.readTree(tripFile);
            ArrayNode trips;
            if (tripRoot.isArray()) {
                trips = (ArrayNode) tripRoot;
            } else if (tripRoot.has("trips") && tripRoot.get("trips").isArray()) {
                trips = (ArrayNode) tripRoot.get("trips");
            } else {
                trips = mapper.createArrayNode();
            }

            ArrayNode removedTripIds = mapper.createArrayNode();
            ArrayNode tripsRemoved = mapper.createArrayNode();
            for (int i = trips.size() - 1; i >= 0; i--) {
                if (transportId.equals(trips.get(i).path("transport").asText())) {
                    tripsRemoved.add(trips.get(i));
                    removedTripIds.add(trips.get(i).path("id").asText());
                    trips.remove(i);
                }
            }
            snapShot.set("tripsRemoved", tripsRemoved);
            mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);

            // Handle Company.json
            ArrayNode tripsFromCompany = mapper.createArrayNode();
            if (removedTripIds.size() > 0) {
                File companyFile = new File("src/Database/Company.json");
                JsonNode companyRoot = mapper.readTree(companyFile);
                ArrayNode companies;
                if (companyRoot.isArray()) {
                    companies = (ArrayNode) companyRoot;
                } else if (companyRoot.has("companies") && companyRoot.get("companies").isArray()) {
                    companies = (ArrayNode) companyRoot.get("companies");
                } else {
                    companies = mapper.createArrayNode();
                }

                for (JsonNode company : companies) {
                    if (!company.has("Trips")) continue;
                    ArrayNode companyTrips = (ArrayNode) company.get("Trips");
                    for (int i = companyTrips.size() - 1; i >= 0; i--) {
                        String tripId = companyTrips.get(i).asText();
                        for (JsonNode removedId : removedTripIds) {
                            if (removedId.asText().equals(tripId)) {
                                tripsFromCompany.add(companyTrips.get(i).asText());
                                companyTrips.remove(i);
                                break;
                            }
                        }
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
            }

            snapShot.set("tripsFromCompany", tripsFromCompany);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/Database/transportDeleteHistory.json"), snapShot);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Unable to delete transport.");
        }
    }
}
