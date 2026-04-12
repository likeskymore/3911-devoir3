package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Airport extends Location {

	public Airport( String city) {
		super(city);
	}
	public Airport(String city, String name) {
		super(city, name);
	}
	public Airport(JsonNode node) {
		super(node.path("city").asText(), node.path("name").asText(node.path("city").asText()));
		setId(node.get("id").asText());
    }
}