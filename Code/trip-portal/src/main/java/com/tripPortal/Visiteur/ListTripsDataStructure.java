package com.tripPortal.Visiteur;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class ListTripsDataStructure {

    private ArrayList<VisitableTrips> trips = new ArrayList<>();

    public void add(VisitableTrips trip) {
        trips.add(trip);
    }

    public ArrayList<javafx.util.Pair<String, JsonNode>> acceptWithNodes(Visitor visitor) {
        ArrayList<javafx.util.Pair<String, JsonNode>> results = new ArrayList<>();
        for (VisitableTrips trip : trips) {
            String display = trip.accept(visitor);
            if (display != null && !display.isBlank()) {
                results.add(new javafx.util.Pair<>(display, trip.getNode()));
            }
        }
        return results;
    }
}