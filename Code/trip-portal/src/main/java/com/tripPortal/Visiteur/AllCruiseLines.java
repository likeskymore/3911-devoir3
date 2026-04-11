package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;

public class AllCruiseLines implements VisitableTrips {
    private JsonNode node;

    public AllCruiseLines(JsonNode node) { this.node = node; }

    @Override
    public String accept(Visitor visitor) { return visitor.visitCruiseLine(node); }

    @Override
    public JsonNode getNode() { return node; }
}