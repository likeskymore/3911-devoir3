package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;

public interface Visitor {
    String visit(JsonNode flightNode);

    String visitRoute(JsonNode routeNode);

    String visitCruiseLine(JsonNode cruiseNode);
}