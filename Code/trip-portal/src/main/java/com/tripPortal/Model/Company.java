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
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Company {

	private String id;
	private String name;
	private String tripID;
	private ArrayList<TransportPrototype> transports;
	private ArrayList<Trip> trips;

	public Company(String name) {
		this.id = randomGenerateID(5);
		this.tripID = randomGenerateID(2);
		this.name = name;
		this.trips = new ArrayList<Trip>();
		this.transports = new ArrayList<TransportPrototype>();

	}

	private String randomGenerateID(int n) {
		String id = "";
		Random rand = new Random();
		if (n == 5) {
			boolean isUnique = false;
			while (!isUnique) {
				id = "";
				for (int i = 0; i < n; i++) {
					char letter = (char) (rand.nextInt(26) + 'A');
					id += letter;
				}

				isUnique = isCompanyIdentifierUnique(id);
			}
		} else {
			for (int i = 0; i < n; i++) {
				char letter = (char) (rand.nextInt(26) + 'A');
				id += letter;
			}
		}

		return id;

	}

	private boolean isCompanyIdentifierUnique(String generatedId) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Company.json");
			if (!file.exists()) {
				return true;
			}

			ArrayNode companies = (ArrayNode) mapper.readTree(file);
			for (JsonNode node : companies) {
				if (node.has("id") && generatedId.equals(node.get("id").asText())) {
					return false;
				}
			}
			return true;
		} catch (IOException ex) {
			return true;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTripID() {
		return tripID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public ArrayList<TransportPrototype> getTransports() {
		return transports;
	}

	public void setTransports(ArrayList<TransportPrototype> transports) {
		this.transports = transports;
	}

	public ArrayList<Trip> getTrips() {
		return trips;
	}

	public void setTrips(ArrayList<Trip> trips) {
		this.trips = trips;
	}

	public static Company fromJson(String name, JsonNode root) {
		for (JsonNode node : root) {
			if (node.get("name").asText().equals(name)) {
				String type = node.get("type").asText();
				return switch (type) {
					case "FlightCompany" -> new FlightCompany(node);
					case "BoatCompany" -> new BoatCompany(node);
					case "TrainCompany" -> new TrainCompany(node);
					default -> throw new IllegalArgumentException("Unknown company type: " + type);
				};
			}
		}
		throw new IllegalArgumentException("Company not found: " + name);
	}

	public void deleteCompany() {
		try {
			ObjectMapper mapper = new ObjectMapper();

			File companyFile = new File("src/Database/Company.json");
			ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
			for (int i = 0; i < companies.size(); i++) {
				if (companies.get(i).path("id").asText().equals(this.id)) {
					companies.remove(i);
					break;
				}
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);

			File tripFile = new File("src/Database/Trip.json");
			ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
			for (int i = trips.size() - 1; i >= 0; i--) {
				if (trips.get(i).path("company").asText().equals(this.name)) {
					trips.remove(i);
				}
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Unable to delete the company.");
			return;
		}
	}

	public void updateName(String newName, String oldName) {
		try {
			ObjectMapper m = new ObjectMapper();
			File f = new File("src/Database/Company.json");
			ArrayNode arr = (ArrayNode) m.readTree(f);
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).get("id").asText().equals(this.getId())) {
					((ObjectNode) arr.get(i)).put("name", newName);
					this.setName(newName);
					break;
				}
			}
			m.writerWithDefaultPrettyPrinter().writeValue(f, arr);
			// Update trips
			File tf = new File("src/Database/Trip.json");
			ArrayNode ta = (ArrayNode) m.readTree(tf);
			for (int i = 0; i < ta.size(); i++) {
				if (ta.get(i).get("company").asText().equals(oldName))
					((ObjectNode) ta.get(i)).put("company", newName);
			}
			m.writerWithDefaultPrettyPrinter().writeValue(tf, ta);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}