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
import com.tripPortal.Model.FlightCompany;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.TrainCompany;

public class TrainCompanyFactory extends TrainTripFactory {
	// Singleton
	private static TrainCompanyFactory instance;

	private TrainCompanyFactory() {}

	public static TrainCompanyFactory getInstance() {
		if (instance == null) {
			instance = new TrainCompanyFactory();
		}
		return instance;
	}

	// Patron de fabrique
	public Location createLocation(String city) {
		// TODO - implement TrainCompanyFactory.createLocation
		return null;
	}


	public Company createCompany(String name) {
		TrainCompany company = new TrainCompany(name);

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Company.json");

			// Read existing array
			JsonNode root = mapper.readTree(file);
			ArrayNode array;

			if (root == null || root.isMissingNode() || root.isNull()) {
				array = mapper.createArrayNode(); // fichier vide ou null
			} else if (root.isArray()) {
				array = (ArrayNode) root;
			} else {
				throw new IOException("Company.json doit contenir un tableau JSON []");
			}
			// Build new entry
			ObjectNode newCompany = mapper.createObjectNode();
			newCompany.put("type", "TrainCompany");
			newCompany.put("id", company.getId());
			newCompany.put("name", name);
			newCompany.put("tripId", company.getTripID());

			// Append and write back
			array.add(newCompany);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

		} catch (IOException e) {
			System.err.println("Failed to write company to JSON: " + e.getMessage());
			e.printStackTrace();
		}

		return company;
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