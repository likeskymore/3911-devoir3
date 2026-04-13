package com.tripPortal.Model;


import com.fasterxml.jackson.databind.JsonNode;

public class FlightCompany extends Company {

	public FlightCompany(String name) {
		super(name);
	}

	public FlightCompany(JsonNode node) {
        super(node.get("name").asText());
        setId(node.get("id").asText());
        setTripID(node.get("tripId").asText());
        }
}