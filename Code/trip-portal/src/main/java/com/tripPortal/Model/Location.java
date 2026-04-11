package com.tripPortal.Model;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
public abstract class Location {
	private String id;
	private String city;

	public Location(String city) {
		this.id = randomGenerateID();
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
}