package com.tripPortal.Mediateur;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Commande.Command;
import com.tripPortal.Factory.AirportFactory;
import com.tripPortal.Factory.PortFactory;
import com.tripPortal.Factory.TrainStationFactory;
import com.tripPortal.Model.Location;

public class locationController {
	
	Command command;

	public void setCommand(Command command){
		this.command = command;
	}

	public void goCallCreateLocation(String city, String name, String type) {

		if ("Airport".equals(type)) {
			AirportFactory.getInstance().createLocation(city, name);
		} else if ("Port".equals(type)) {
			PortFactory.getInstance().createLocation(city, name);
		} else if ("Station".equals(type)) {
			TrainStationFactory.getInstance().createLocation(city, name);
		} else {
			System.err.println("Unknown location type: " + type);
		}
		
	}

	public void goCallCreateLocation(String city, String type) {
		goCallCreateLocation(city, city, type);
	}

	/**
	 * 
	 * @param location
	 * @param city
	 */
	// public boolean editCity(Location location, String city) {
	// 	return editLocation(location, city, location.getName());
	// }

	// public boolean editLocation(Location location, String city, String name) {
	// 	try {
	// 		ObjectMapper mapper = new ObjectMapper();
	// 		File file = new File("src/Database/Location.json");
	// 		ArrayNode locations = (ArrayNode) mapper.readTree(file);

	// 		for (int i = 0; i < locations.size(); i++) {
	// 			JsonNode node = locations.get(i);
	// 			if (node.path("id").asText().equals(location.getId())) {
	// 				((ObjectNode) node).put("city", city);
	// 				((ObjectNode) node).put("name", name);
	// 				location.setCity(city);
	// 				location.setName(name);
	// 				mapper.writerWithDefaultPrettyPrinter().writeValue(file, locations);
	// 				return true;
	// 			}
	// 		}
	// 		System.err.println("Location not found: " + location.getId());
	// 	} catch (IOException ex) {
	// 		ex.printStackTrace();
	// 	}
	// 	return false;
	// }

	// /**
	//  * 
	//  * @param location
	//  */
	// public boolean deleteLocation(Location location) {
	// 	try {
	// 		ObjectMapper mapper = new ObjectMapper();

	// 		File locationFile = new File("src/Database/Location.json");
	// 		ArrayNode locations = (ArrayNode) mapper.readTree(locationFile);
	// 		for (int i = 0; i < locations.size(); i++) {
	// 			if (locations.get(i).path("id").asText().equals(location.getId())) {
	// 				locations.remove(i);
	// 				break;
	// 			}
	// 		}
	// 		mapper.writerWithDefaultPrettyPrinter().writeValue(locationFile, locations);

	// 		File tripFile = new File("src/Database/Trip.json");
	// 		ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
	// 		ArrayNode removedTripIds = mapper.createArrayNode();
	// 		for (int i = trips.size() - 1; i >= 0; i--) {
	// 			JsonNode trip = trips.get(i);
	// 			boolean referencesLocation = false;
	// 			if (location.getId().equals(trip.path("origin").asText()) || location.getId().equals(trip.path("destination").asText())) {
	// 				referencesLocation = true;
	// 			} else if (trip.has("path")) {
	// 				for (JsonNode stop : trip.get("path")) {
	// 					if (location.getId().equals(stop.asText())) {
	// 						referencesLocation = true;
	// 						break;
	// 					}
	// 				}
	// 			}

	// 			if (referencesLocation) {
	// 				removedTripIds.add(trip.path("id").asText());
	// 				trips.remove(i);
	// 			}
	// 		}
	// 		mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);

	// 		if (removedTripIds.size() > 0) {
	// 			File companyFile = new File("src/Database/Company.json");
	// 			ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
	// 			for (JsonNode company : companies) {
	// 				if (!company.has("Trips")) {
	// 					continue;
	// 				}
	// 				ArrayNode companyTrips = (ArrayNode) company.get("Trips");
	// 				for (int i = companyTrips.size() - 1; i >= 0; i--) {
	// 					String tripId = companyTrips.get(i).asText();
	// 					for (JsonNode removedTripId : removedTripIds) {
	// 						if (removedTripId.asText().equals(tripId)) {
	// 							companyTrips.remove(i);
	// 							break;
	// 						}
	// 					}
	// 				}
	// 			}
	// 			mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
	// 		}

	// 		return true;
	// 	} catch (IOException ex) {
	// 		ex.printStackTrace();
	// 	}
	// 	return false;
	// }

	public void updateLocation(){
		command.execute();
	}

	public void deleteLocation(){
		command.execute();
	}

	public void undoUpdateLocation(){
		command.undo();
	}

}