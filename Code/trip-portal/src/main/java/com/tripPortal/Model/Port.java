package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Port extends Location {
	public Port(String city) {
        super(city);
    }
    public Port(String city, String name) {
        super(city, name);
    }
    public Port(JsonNode node) {
        super(node.path("city").asText(), node.path("name").asText(node.path("city").asText()));
        setId(node.get("id").asText());
    }
}