package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class PortFactory extends BoatTripFactory {

	// Singleton
    private static PortFactory instance;

    private PortFactory() {}

    public static PortFactory getInstance() {
        if (instance == null) {
            instance = new PortFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		// TODO - implement PortFactory.createLocation
		Location p = new Port(city);
		return p;
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
        Transport transport
    ){
		return null;
	}

}