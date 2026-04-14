package com.tripPortal.Model;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
public abstract class Location {
	private String id;
	private String city;
	private String name;

	public Location(String city) {
		this(city, city);
	}

	public Location(String city, String name) {
		this.id = randomGenerateID();
		this.city = city;
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String randomGenerateID() {
		String id = "";
		Random rand = new Random();
		for (int i = 0; i < 3; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// complete verification ...

		return id;

	}

	public static Location fromJson(String city, JsonNode root) {
		for (JsonNode node : root) {
			if (!node.has("city") || !node.has("type")) continue;
			if (node.get("city").asText().equals(city)) {
				String type = node.get("type").asText();
				return switch (type) {
					case "Airport"      -> new Airport(node);
					case "Port"         -> new Port(node);
					case "TrainStation" -> new TrainStation(node);
					default -> throw new IllegalArgumentException("Unknown location type: " + type);
				};
			}
		}
		throw new IllegalArgumentException("Location not found: " + city);
	}

	public void update(String newName, String newCity){
		 if (newCity == null || newCity.isBlank()) {
			System.err.println("City cannot be empty.");
			return;
		}
		if (newName == null || newName.isBlank()) {
			System.err.println("Location name cannot be empty.");
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("src/Database/Location.json");
			ArrayNode locations = (ArrayNode) mapper.readTree(file);
			ObjectNode snapShot = mapper.createObjectNode();
			File restoreFile = new File("src/Database/locationUpdateHistory.json");
			for (int i = 0; i < locations.size(); i++) {
				JsonNode node = locations.get(i);
				if (node.path("id").asText().equals(this.getId())) {
					snapShot.put("id",this.getId());
					snapShot.set("name", node.get("name"));
					snapShot.set("city", node.get("city"));
					((ObjectNode) node).put("city", newCity);
					((ObjectNode) node).put("name", newName);
					this.setCity(newCity);
					this.setName(newName);
					mapper.writerWithDefaultPrettyPrinter().writeValue(restoreFile, snapShot);
					mapper.writerWithDefaultPrettyPrinter().writeValue(file, locations);
					return;
				}
			}
			System.err.println("Location not found: " + this.getId());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return;
	}

	public void delete(){
		try {
			ObjectMapper mapper = new ObjectMapper();

			File locationFile = new File("src/Database/Location.json");
			ArrayNode locations = (ArrayNode) mapper.readTree(locationFile);
			ObjectNode snapShot = mapper.createObjectNode();

			for (int i = 0; i < locations.size(); i++) {
				if (locations.get(i).path("id").asText().equals(this.getId())) {
					snapShot.set("location",locations.get(i));
					locations.remove(i);
					break;
				}
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(locationFile, locations);

			File tripFile = new File("src/Database/Trip.json");
			ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
			ArrayNode removedTripIds = mapper.createArrayNode();
			ArrayNode tripsRemoved = mapper.createArrayNode();

			for (int i = trips.size() - 1; i >= 0; i--) {
				JsonNode trip = trips.get(i);
				boolean referencesLocation = false;
				if (this.getId().equals(trip.path("origin").asText()) || this.getId().equals(trip.path("destination").asText())) {
					referencesLocation = true;
				} else if (trip.has("path")) {
					for (JsonNode stop : trip.get("path")) {
						if (this.getId().equals(stop.asText())) {
							referencesLocation = true;
							break;
						}
					}
				}

				if (referencesLocation) {
					tripsRemoved.add(trip);
					removedTripIds.add(trip.path("id").asText());
					trips.remove(i);
				}
			}

			snapShot.set("tripsRemoved", tripsRemoved);
			mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);

			ArrayNode tripsFromCompany = mapper.createArrayNode();

			if (removedTripIds.size() > 0) {
				File companyFile = new File("src/Database/Company.json");
				ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
				for (JsonNode company : companies) {
					if (!company.has("Trips")) {
						continue;
					}
					ArrayNode companyTrips = (ArrayNode) company.get("Trips");
					for (int i = companyTrips.size() - 1; i >= 0; i--) {
						String tripId = companyTrips.get(i).asText();
						for (JsonNode removedTripId : removedTripIds) {
							if (removedTripId.asText().equals(tripId)) {
								tripsFromCompany.add(companyTrips.get(i));
								companyTrips.remove(i);
								break;
							}
						}
					}
				}
				snapShot.set("tripsFromCompany", tripsFromCompany);
				mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
			}

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/Database/locationDeleteHistory.json"), tripsFromCompany);

			return;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return;
	}
}