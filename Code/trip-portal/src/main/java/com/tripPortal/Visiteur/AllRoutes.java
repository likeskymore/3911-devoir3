package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;

public class AllRoutes implements VisitableTrips {
    private JsonNode node;

    public AllRoutes(JsonNode node) {
        this.node = node;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitRoute(node);
    }

    @Override
    public JsonNode getNode() {
        return node;
    }
}