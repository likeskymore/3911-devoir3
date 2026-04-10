package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class BoatCompany extends Company {

    public BoatCompany(String name) {
        super(name);
    }

    public BoatCompany(JsonNode node) {
        super(node.get("name").asText());
        setId(node.get("id").asText());
        setTripID(node.get("tripID").asText());
    }
}
