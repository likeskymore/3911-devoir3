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
import com.tripPortal.Model.FlightCompany;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class AirportFactory extends PlaneTripFactory {

	// Singleton
    private static AirportFactory instance;

    private AirportFactory() {}

    public static AirportFactory getInstance() {
        if (instance == null) {
            instance = new AirportFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		Airport airport = new Airport(city);

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Location.json");

			// Read existing array
			JsonNode root = mapper.readTree(file);
			ArrayNode array = (ArrayNode) root;

			// Build new entry
			ObjectNode newAirport = mapper.createObjectNode();
			newAirport.put("type", "Airport");
			newAirport.put("id", airport.getId());
			newAirport.put("city", city);

			// Append and write back
			array.add(newAirport);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

		} catch (IOException e) {
			System.err.println("Failed to write airport to JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return airport;
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