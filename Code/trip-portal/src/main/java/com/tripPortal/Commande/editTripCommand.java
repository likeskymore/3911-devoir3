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
			throw new IllegalStateException("Unable to read trip update history.", e);
		}

		if (oldTrip == null || !oldTrip.isObject() || oldTrip.path("id").asText("").isBlank()) {
			throw new IllegalStateException("No trip update to undo.");
		}
		
		ObjectMapper tripMapper = new ObjectMapper();
		File tripFile = new File("src/Database/Trip.json");
		ArrayNode trips = null;
		try {
			JsonNode tripRoot = tripMapper.readTree(tripFile);
			if (tripRoot != null && tripRoot.isArray()) {
				trips = (ArrayNode) tripRoot;
			} else {
				trips = tripMapper.createArrayNode();
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read trips.", e);
		}

		String companyNameToRemove = null;
		String tripId = oldTrip.get("id").asText();
		boolean tripFound = false;
		for (int i = 0; i < trips.size(); i++){
			if (trips.get(i).get("id").asText().equals(oldTrip.get("id").asText())){
				tripFound = true;
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

		if (!tripFound) {
			throw new IllegalStateException("Cannot undo trip update because the trip no longer exists. Undo delete first.");
		}

		ObjectMapper companyMapper = new ObjectMapper();
		File companyFile = new File("src/Database/Company.json");
		ArrayNode companies = null;
		try {
			JsonNode companyRoot = companyMapper.readTree(companyFile);
			if (companyRoot != null && companyRoot.isArray()) {
				companies = (ArrayNode) companyRoot;
			} else {
				companies = companyMapper.createArrayNode();
			}
		} catch (Exception e) {
			throw new IllegalStateException("Unable to read companies.", e);
		}

		if (companyNameToRemove != null && !companyNameToRemove.equals(oldTrip.get("company").asText())){
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
			throw new IllegalStateException("Unable to persist undo update.", e);
		}

	}

}