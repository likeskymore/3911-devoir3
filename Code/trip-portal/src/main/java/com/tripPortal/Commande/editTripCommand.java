package com.tripPortal.Commande;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Trip;

public class editTripCommand implements Command {

	private Trip trip;
	private String newCompany;
	private LocalDate newStartDate;
	private LocalDate newEndDate;
	private float newPrice;
	private String newTransportId;

	public editTripCommand(Trip trip, String newCompany, LocalDate newStartDate, LocalDate newEndDate,
			float newPrice, String newTransportId){
		this.trip = trip;
		this.newCompany = newCompany;
		this.newStartDate = newStartDate;
		this.newEndDate = newEndDate;
		this.newPrice = newPrice;
		this.newTransportId = newTransportId;
	}

	public editTripCommand(){
		//this one is for undo
	}
	public void execute() {
		trip.updateTrip(newCompany, newStartDate, newEndDate, newPrice, newTransportId);
	}

	public void undo() {
		ObjectMapper updateTripMapper = new ObjectMapper();
		File updateTripFile = new File("src/Database/tripUpdateHistory.json");
		JsonNode oldTrip = null;
		try {
			oldTrip = updateTripMapper.readTree(updateTripFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectMapper tripMapper = new ObjectMapper();
		File tripFile = new File("src/Database/Trip.json");
		ArrayNode trips = null;
		try {
			trips = (ArrayNode) updateTripMapper.readTree(tripFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String companyNameToRemove = null;
		String tripId = oldTrip.get("id").asText();
		for (int i = 0; i < trips.size(); i++){
			if (trips.get(i).get("id").asText().equals(oldTrip.get("id").asText())){
				companyNameToRemove = trips.get(i).get("company").asText();
				ObjectNode newTrip = (ObjectNode) trips.get(i);
				newTrip.put("company", oldTrip.get("company").asText());
				newTrip.put("startDate", oldTrip.get("startDate").asText());
				newTrip.put("endDate", oldTrip.get("endDate").asText());
				newTrip.put("price", oldTrip.get("price").floatValue());
				newTrip.put("transport", oldTrip.get("transport").asText());
				newTrip.put("duration", oldTrip.get("duration").asInt());

			}
		}

		ObjectMapper companyMapper = new ObjectMapper();
		File companyFile = new File("src/Database/Company.json");
		ArrayNode companies = null;
		try {
			companies = (ArrayNode) companyMapper.readTree(companyFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!companyNameToRemove.equals(oldTrip.get("company").asText())){
			for (int i = 0; i < companies.size(); i++){
				if (companies.get(i).get("name").asText().equals(companyNameToRemove)){
					ArrayNode companyTrips = (ArrayNode) companies.get(i).get("Trips");
					for (int j = 0; j < companyTrips.size(); j++){
						if (companyTrips.get(j).asText().equals(tripId)){
							companyTrips.remove(j);
							break;
						}
					}
				}

				else if (companies.get(i).get("name").asText().equals(oldTrip.get("company").asText())){
					ArrayNode companyTrips = (ArrayNode) companies.get(i).get("Trips");
					companyTrips.add(tripId);
				}
			}
		}

		try {
			tripMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
			companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
			updateTripMapper.writerWithDefaultPrettyPrinter().writeValue(updateTripFile, updateTripMapper.createObjectNode());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}