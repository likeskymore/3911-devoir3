package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Boat extends Transport{
    public Boat(boolean available) {
        super(available);
    }

    public Boat(JsonNode node) {
        super(node.get("available").asBoolean());
        this.TransportID = node.get("transportID").asText();
    }
}
