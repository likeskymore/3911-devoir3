package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Airport extends Location {

	public Airport( String city) {
		super(city);
	}
	public Airport(JsonNode node) {
        super(node.get("city").asText());
		setId(node.get("id").asText());
    }
}