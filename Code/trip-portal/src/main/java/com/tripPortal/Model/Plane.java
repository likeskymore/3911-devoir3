package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Plane extends Transport {

    public Plane(boolean available) {
        super(available);
    }
    public Plane(JsonNode node) {
        super(node.get("available").asBoolean());
        this.TransportID = node.get("transportID").asText();
    }
}
