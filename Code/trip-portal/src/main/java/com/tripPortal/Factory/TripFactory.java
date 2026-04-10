package com.tripPortal.Factory;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class TripFactory {

	public static TripFactory getFactory(String transportType) {
        return switch (transportType) {
            case "Flight" -> new FlightFactory();
            case "Cruise" -> new CruiseLineFactory();
            case "Train"  -> new RouteFactory();
            default -> throw new IllegalArgumentException("Unknown transport: " + transportType);
        };
    }

    public abstract Location createLocation(String city);
    public abstract Company createCompany(String name);
	public abstract Trip createTrajectory(
		Company company,
		LocalDate startDate,
		LocalDate endDate,
		float price,
		int duration,
		ArrayList<Location> locations,
		Transport transport
	);
	

}