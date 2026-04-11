package com.tripPortal.Factory;

import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;

public class FlightFactory extends PlaneTripFactory {

    // Singleton
    private static FlightFactory instance;

    private FlightFactory() {}

    public static FlightFactory getInstance() {
        if (instance == null) {
            instance = new FlightFactory();
        }
        return instance;
    }

    // Patron de fabrique
    public Location createLocation(String city) {
        return null;
    }

    public Company createCompany(String name) {
        return null;
    }

    public Trip createTrajectory(
            Company company,
            LocalDate startDate,
            LocalDate endDate,
            float price,
            int duration,
            ArrayList<Location> locations,
            Transport transport) {

        if (!(locations.get(0) instanceof Airport) || !(locations.get(locations.size() - 1) instanceof Airport)) {
            throw new IllegalArgumentException("FlightFactory requires Airport locations");
        }
        if (!(transport instanceof Plane)) {
            throw new IllegalArgumentException("FlightFactory requires a Plane transport");
        }

        return new Flight(
            company,
            startDate,
            endDate,
            price,
            duration,
            (Airport) locations.get(0),
            (Airport) locations.get(locations.size() - 1),
            (Plane) transport
        );
    }
}