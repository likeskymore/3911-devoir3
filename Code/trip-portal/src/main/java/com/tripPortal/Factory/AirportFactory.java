package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class AirportFactory extends PlaneTripFactory {

	// Singleton
    private static AirportFactory instance;

    private AirportFactory() {}

    public static AirportFactory getInstance() {
        if (instance == null) {
            instance = new AirportFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		// TODO - implement AirportFactory.createLocation
		Location l = new Airport( city );
		return l;
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