package com.tripPortal.Factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.CruiseLine;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class CruiseLineFactory extends BoatTripFactory {

	// Singleton
    private static CruiseLineFactory instance;

    private CruiseLineFactory() {}

    public static CruiseLineFactory getInstance() {
        if (instance == null) {
            instance = new CruiseLineFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
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

    if (!(transport instanceof Boat)) {
        throw new IllegalArgumentException("CruiseLineFactory requires a Boat transport");
    }

    ArrayList<Port> ports = new ArrayList<>();
    for (Location loc : locations) {
        if (!(loc instanceof Port))
            throw new IllegalArgumentException("CruiseLineFactory requires Port locations");
        ports.add((Port) loc);
    }

    Boat boat = (Boat) transport;
    CruiseLine cruise = new CruiseLine(company, startDate, endDate, price, duration, ports, boat);

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
        node.put("type",      "CruiseLine");
        node.put("id",        cruise.getId());
        node.put("company",   company.getName());
        node.put("startDate", startDate.toString());
        node.put("endDate",   endDate.toString());
        node.put("price",     price);
        node.put("duration",  duration);
        node.put("transport", boat.getTransportID());
        node.put("active", 1);

        ArrayNode pathArray = mapper.createArrayNode();
        for (Port port : ports) pathArray.add(port.getId());
        node.set("path", pathArray);

        JsonNode CompanyRoot = mapper.readTree(companyFile);
        ArrayNode companyArray = (ArrayNode) CompanyRoot;

        for (JsonNode companyNode : companyArray){
            if (companyNode.get("id").asText().equals(company.getId())){
                ArrayNode trips = (ArrayNode) companyNode.get("Trips");
                trips.add(mapper.valueToTree(cruise));
                break;
            }
        }
        array.add(node);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);
        mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companyArray);

    } catch (IOException e) {
        System.err.println("Failed to write CruiseLine to JSON: " + e.getMessage());
        e.printStackTrace();
    }

    return cruise;
}

}