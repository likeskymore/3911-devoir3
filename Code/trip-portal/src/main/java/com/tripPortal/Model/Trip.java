package com.tripPortal.Model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tripPortal.Factory.TripFactory;

public abstract class Trip {

	private String id;
	private Company servicedBy;
	private LocalDate departureTime;
	private LocalDate arrivalTime;
	private float price;
	private int tripDuration;
	private TripFactory tripFactory;
	private Boolean active;

	public Trip(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime, float price, int tripDuration) {
		this.id = randomGenerateID(servicedBy);
		this.servicedBy = servicedBy;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
		this.tripDuration = tripDuration;
		this.active = true;
	}

	public Trip(String id){
		this.id = id;
	}

	private String randomGenerateID(Company servicedBy) {
		String id = servicedBy.getTripID();
		Random rand = new Random();

		for (int i = 0; i < 4; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// complete verification ...

		return id;

	}
	public String getId() {
		return id;
	}

	public Company getServicedBy() {
		return servicedBy;
	}

	public LocalDate getDepartureTime() {
		return departureTime;
	}

	public LocalDate getArrivalTime() {
		return arrivalTime;
	}

	public float getPrice() {
		return price;
	}

	public int getTripDuration() {
		return tripDuration;
	}

	public void delete() throws IOException{
		ObjectMapper displayMapper = new ObjectMapper();
		File tripFile = new File("src/Database/Trip.json");
		ArrayNode tripArray = (ArrayNode) displayMapper.readTree(tripFile);
		for (int i = 0; i < tripArray.size(); i++) {
			if (tripArray.get(i).get("id").asText().equals(this.id)) { 
				tripArray.remove(i); break; 
			}
		}
		try { displayMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, tripArray); } catch (IOException ex) { ex.printStackTrace(); }
		try {
			ObjectMapper cm = new ObjectMapper();
			File cf = new File("src/Database/Company.json");
			ArrayNode ca = (ArrayNode) cm.readTree(cf);
			for (JsonNode cn : ca) {
				ArrayNode tn = (ArrayNode) cn.get("Trips");
				for (int i = 0; i < tn.size(); i++) { if (tn.get(i).asText().equals(this.id)) { tn.remove(i); break; } }
			}
			cm.writerWithDefaultPrettyPrinter().writeValue(cf, ca);
		} catch (IOException ex) { ex.printStackTrace(); }
}


}