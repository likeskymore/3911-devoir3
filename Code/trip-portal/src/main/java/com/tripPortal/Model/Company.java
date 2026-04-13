package com.tripPortal.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;

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
		
	}
}