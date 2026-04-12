package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;

public interface VisitableTrips {
    String accept(Visitor visitor);
    JsonNode getNode();
}