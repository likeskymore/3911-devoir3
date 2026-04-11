package com.tripPortal.Visiteur;

import java.util.ArrayList;

public class ListTripsDataStructure {

    private ArrayList<VisitableTrips> trips = new ArrayList<>();

    public void add(VisitableTrips trip) { trips.add(trip); }

    public ArrayList<String> accept(Visitor visitor) {
        ArrayList<String> results = new ArrayList<>();
        for (VisitableTrips trip : trips) {
            results.add(trip.accept(visitor));
        }
        return results;
    }
}