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
	private ArrayList<Transport> transports;
	private ArrayList<Trip> trips;

	public Company(String name) {
		this.id = randomGenerateID(5);
		this.tripID = randomGenerateID(2);
		this.name = name;
		this.trips = new ArrayList<Trip>();
		this.transports = new ArrayList<Transport>();

	}

	public Company(){
		//this is for undo
	}

	private String randomGenerateID(int n) {
		String id = "";
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// complete verification ...

		return id;

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

	public ArrayList<Transport> getTransports() {
		return transports;
	}
	
	public void setTransports(ArrayList<Transport> transports) {
		this.transports = transports;
	}

	public ArrayList<Trip> getTrips() {
		return trips;
	}

	public void setTrips(ArrayList<Trip> trips) {
		this.trips = trips;
	}

	public static Company fromJson(String name, JsonNode root) {
		for (JsonNode node : root) { // root is a flat array, just iterate directly
			if (node.get("name").asText().equals(name)) {
				String type = node.get("type").asText();
				return switch (type) {
					case "FlightCompany" -> new FlightCompany(node);
					case "BoatCompany"   -> new BoatCompany(node);
					case "TrainCompany"  -> new TrainCompany(node);
					default -> throw new IllegalArgumentException("Unknown company type: " + type);
				};
			}
		}
		throw new IllegalArgumentException("Company not found: " + name);
	}

	public void deleteCompany(){
		try {
			ObjectMapper mapper = new ObjectMapper();
			File companyFile = new File("src/Database/Company.json");
			ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);

			ObjectNode snapShot = mapper.createObjectNode();

			for (int i = 0; i < companies.size(); i++) {
				if (companies.get(i).path("id").asText().equals(this.id)) {
					snapShot.set("company", companies.get(i));
					companies.remove(i);
					break;
				}
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);

			File tripFile = new File("src/Database/Trip.json");
			ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
			ArrayNode deletedTrips = mapper.createArrayNode();
			for (int i = trips.size() - 1; i >= 0; i--) {
				if (trips.get(i).path("company").asText().equals(this.name)) {
					deletedTrips.add(trips.get(i));
					trips.remove(i);
				}
			}
			snapShot.set("trips", deletedTrips);
			mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/Database/companyDeleteHistory.json"), snapShot);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Unable to delete the company.");
			return;
		}
	}

	public void updateName(String newName, String oldName){
		try {
			ObjectMapper m = new ObjectMapper();
			File f = new File("src/Database/Company.json");
			ArrayNode arr = (ArrayNode) m.readTree(f);
			ObjectNode snapShot = m.createObjectNode();
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).get("id").asText().equals(this.getId())) {
					snapShot.set("oldCompanyName", arr.get(i).get("name"));
					snapShot.put("newCompanyName", newName);
					((ObjectNode) arr.get(i)).put("name", newName);
					this.setName(newName); break;
				}
			}
			m.writerWithDefaultPrettyPrinter().writeValue(f, arr);
			ArrayNode tripsToUpdate = m.createArrayNode();
			// Update trips
			File tf = new File("src/Database/Trip.json");
			ArrayNode ta = (ArrayNode) m.readTree(tf);
			for (int i = 0; i < ta.size(); i++) {
				if (ta.get(i).get("company").asText().equals(oldName)){
					tripsToUpdate.add(ta.get(i).get("id"));
					((ObjectNode) ta.get(i)).put("company", newName);
				}
			}
			snapShot.set("tripsToUpdate", tripsToUpdate);
			m.writerWithDefaultPrettyPrinter().writeValue(tf, ta);
			m.writerWithDefaultPrettyPrinter().writeValue(new File("src/Database/companyUpdateNameHistory.json"), snapShot);
		} catch (IOException ex) { ex.printStackTrace(); }
	}
}