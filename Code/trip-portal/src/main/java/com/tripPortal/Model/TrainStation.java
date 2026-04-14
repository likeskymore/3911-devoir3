package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class TrainStation extends Location {
	public TrainStation(String city) {
		super(city);
	}

	public TrainStation(String city, String name) {
		super(city, name);
	}

	public TrainStation(JsonNode node) {
		super(node.path("city").asText(), node.path("name").asText(node.path("city").asText()));
		setId(node.get("id").asText());
	}
}