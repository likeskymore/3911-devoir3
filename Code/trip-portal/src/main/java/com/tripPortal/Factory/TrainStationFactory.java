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
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class TrainStationFactory extends TrainTripFactory {

	// Singleton
    private static TrainStationFactory instance;

    private TrainStationFactory() {}

    public static TrainStationFactory getInstance() {
        if (instance == null) {
            instance = new TrainStationFactory();
        }
        return instance;
    }

    // Patron de fabrique
	public Location createLocation(String city) {
		TrainStation trainStation = new TrainStation(city);

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Location.json");

			// Read existing array
			JsonNode root = mapper.readTree(file);
			ArrayNode array = (ArrayNode) root;

			// Build new entry
			ObjectNode newTrainStation = mapper.createObjectNode();
			newTrainStation.put("type", "TrainStation");
			newTrainStation.put("id", trainStation.getId());
			newTrainStation.put("city", city);

			// Append and write back
			array.add(newTrainStation);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

		} catch (IOException e) {
			System.err.println("Failed to write train station to JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return trainStation;
	}
    public Company createCompany(String name) {
        // TODO - implement TrainStationFactory.createCompany
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