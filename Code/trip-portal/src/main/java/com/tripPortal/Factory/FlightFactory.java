package com.tripPortal.Factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class FlightFactory extends PlaneTripFactory {

    // Singleton
    private static FlightFactory instance;

    private FlightFactory() {}

    public static FlightFactory getInstance() {
        if (instance == null) {
            instance = new FlightFactory();
        }
        return instance;
    }

    // Patron de fabrique
    public Location createLocation(String city) {
        return null;
    }

    public Company createCompany(String name) {
        return null;
    }

    public Trip createTrajectory(
        Company company,
        LocalDate startDate,
        LocalDate endDate,
        float price,
        int duration,
        ArrayList<Location> locations,
        Transport transport) {

    if (!(locations.get(0) instanceof Airport) || !(locations.get(locations.size() - 1) instanceof Airport)) {
        throw new IllegalArgumentException("FlightFactory requires Airport locations");
    }
    if (!(transport instanceof Plane)) {
        throw new IllegalArgumentException("FlightFactory requires a Plane transport");
    }

    Airport origin      = (Airport) locations.get(0);
    Airport destination = (Airport) locations.get(locations.size() - 1);
    Plane plane         = (Plane) transport;

    Flight flight = new Flight(company, startDate, endDate, price, duration, origin, destination, plane);

    // ── Sauvegarder en JSON ────────────────────────────────────────
    try {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/Database/Trip.json");
        File companyFile = new File("src/Database/Company.json");

        JsonNode root = mapper.readTree(file);
        ArrayNode array;

        if (root == null || root.isMissingNode() || root.isNull()) {
            array = mapper.createArrayNode(); // fichier vide ou null
        } else if (root.isArray()) {
            array = (ArrayNode) root;
        } else {
            throw new IOException("Trip.json doit contenir un tableau JSON []");
        }

        ObjectNode node = mapper.createObjectNode();
        node.put("type",        "Flight");
        node.put("id",          flight.getId());
        node.put("company",     company.getName());
        node.put("startDate",   startDate.toString());
        node.put("endDate",     endDate.toString());
        node.put("price",       price);
        node.put("duration",    duration);
        node.put("origin",      origin.getCity());
        node.put("destination", destination.getCity());
        node.put("transport",   plane.getTransportID());
        node.put("active", 1);

        JsonNode CompanyRoot = mapper.readTree(companyFile);
        ArrayNode companyArray = (ArrayNode) CompanyRoot;

        for (JsonNode companyNode : companyArray){
            if (companyNode.get("id").asText().equals(company.getId())){
                ArrayNode trips = (ArrayNode) companyNode.get("Trips");
                trips.add(mapper.valueToTree(flight));
                break;
            }
        }
        array.add(node);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);
        mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companyArray);

    } catch (IOException e) {
        System.err.println("Failed to write Flight to JSON: " + e.getMessage());
        e.printStackTrace();
    }

    
    return flight;
}
}