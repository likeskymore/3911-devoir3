package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class TrainStation extends Location {
	public TrainStation(String city) {
		super(city);
	}
	public TrainStation(JsonNode node) {
        super(node.get("city").asText());
        setId(node.get("id").asText());
    }
}