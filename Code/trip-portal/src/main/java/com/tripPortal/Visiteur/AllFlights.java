package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;

public class AllFlights implements VisitableTrips {
    private JsonNode node;

    public AllFlights(JsonNode node) { this.node = node; }

    @Override
    public String accept(Visitor visitor) { return visitor.visit(node); }

    @Override
    public JsonNode getNode() { return node; }
}