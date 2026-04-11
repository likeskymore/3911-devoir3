package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Train extends Transport{

    public Train(boolean available) {
        super(available);
    }
    public Train(JsonNode node) {
        super(node.get("available").asBoolean());
        this.TransportID = node.get("transportID").asText();
    }
}
