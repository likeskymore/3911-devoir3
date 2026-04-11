package com.tripPortal.Factory;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;

public class RouteFactory extends TrainTripFactory {

    // Singleton
    private static RouteFactory instance;

    private RouteFactory() {}

    public static RouteFactory getInstance() {
        if (instance == null) {
            instance = new RouteFactory();
        }
        return instance;
    }

    // Patron de fabrique
	public Location createLocation(String city){
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
        Transport transport
    ){
		// TODO - implement RouteFactory.createTrajectory
		return null;
	}

}