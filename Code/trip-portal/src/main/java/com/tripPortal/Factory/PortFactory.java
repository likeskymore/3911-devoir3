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
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class PortFactory extends BoatTripFactory {

	// Singleton
    private static PortFactory instance;

    private PortFactory() {}

    public static PortFactory getInstance() {
        if (instance == null) {
            instance = new PortFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		Port port = new Port(city);

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Location.json");

			// Read existing array
			JsonNode root = mapper.readTree(file);
			ArrayNode array = (ArrayNode) root;

			// Build new entry
			ObjectNode newPort = mapper.createObjectNode();
			newPort.put("type", "Port");
			newPort.put("id", port.getId());
			newPort.put("city", city);
			
			// Append and write back
			array.add(newPort);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

		} catch (IOException e) {
			System.err.println("Failed to write port to JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return port;
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
        Transport transport
    ){
		return null;
	}

}