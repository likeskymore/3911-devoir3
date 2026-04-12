package com.tripPortal.Factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Route;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class RouteFactory extends TrainTripFactory {

    // Singleton
    private static RouteFactory instance;

    private RouteFactory() {}

    public static RouteFactory getInstance() {
        if (instance == null) {
            instance = new RouteFactory();
        }
        return instance;
    }

    // Patron de fabrique
	public Location createLocation(String city){
		return null;
	}
    public Company createCompany(String name){
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

    if (!(transport instanceof Train)) {
        throw new IllegalArgumentException("RouteFactory requires a Train transport");
    }

    ArrayList<TrainStation> stations = new ArrayList<>();
    for (Location loc : locations) {
        if (!(loc instanceof TrainStation))
            throw new IllegalArgumentException("RouteFactory requires TrainStation locations");
        stations.add((TrainStation) loc);
    }

    Train train = (Train) transport;
    Route route = new Route(company, startDate, endDate, price, duration, stations, train);

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
        node.put("type",      "Route");
        node.put("id",        route.getId());
        node.put("company",   company.getName());
        node.put("startDate", startDate.toString());
        node.put("endDate",   endDate.toString());
        node.put("price",     price);
        node.put("duration",  duration);
        node.put("transport", train.getTransportID());
        node.put("active", 1);

        ArrayNode stationsArray = mapper.createArrayNode();
        for (TrainStation st : stations) stationsArray.add(st.getId());
        node.set("path", stationsArray);

        JsonNode CompanyRoot = mapper.readTree(companyFile);
        ArrayNode companyArray = (ArrayNode) CompanyRoot;

        for (JsonNode companyNode : companyArray){
            if (companyNode.get("id").asText().equals(company.getId())){
                ArrayNode trips = (ArrayNode) companyNode.get("Trips");
                trips.add(mapper.valueToTree(route));
                break;
            }
        }
        array.add(node);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);
        mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companyArray);

    } catch (IOException e) {
        System.err.println("Failed to write Route to JSON: " + e.getMessage());
        e.printStackTrace();
    }

    return route;
}

}