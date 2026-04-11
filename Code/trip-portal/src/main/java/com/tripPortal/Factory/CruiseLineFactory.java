package com.tripPortal.Factory;

import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.CruiseLine;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.Port;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CruiseLineFactory extends BoatTripFactory {

	// Singleton
    private static CruiseLineFactory instance;

    private CruiseLineFactory() {}

    public static CruiseLineFactory getInstance() {
        if (instance == null) {
            instance = new CruiseLineFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		return null;
	}
    public Company createCompany(String name){
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

        if (!(transport instanceof Boat)) {
            throw new IllegalArgumentException("CruiseLineFactory requires a Boat transport");
        }

		ArrayList<Port> ports = new ArrayList<>();
		for (Location l : locations) {
			if (!(l instanceof Port))
				throw new IllegalArgumentException("CruiseFactory requires Port locations");
			ports.add((Port) l);
		}

		return new CruiseLine(
			company,
			startDate,
			endDate,
			price,
			duration,
			ports,
			(Boat) transport
		);
    }

}