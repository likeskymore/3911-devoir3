package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Port extends Location {
	public Port(String city) {
        super(city);
    }
    public Port(JsonNode node) {
        super(node.get("city").asText());
        setId(node.get("id").asText());
    }
}