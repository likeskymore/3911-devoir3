package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.Location;

public class TrainCompanyFactory extends TrainTripFactory {
	// Singleton
	private static TrainCompanyFactory instance;

	private TrainCompanyFactory() {}

	public static TrainCompanyFactory getInstance() {
		if (instance == null) {
			instance = new TrainCompanyFactory();
		}
		return instance;
	}

	// Patron de fabrique
	public Location createLocation(String city) {
		// TODO - implement TrainCompanyFactory.createLocation
		return null;
	}
	/**
	 * 
	 * @param name
	 */
	public Company createCompany(String name) {
		// TODO - implement TrainCompanyFactory.createCompany
		throw new UnsupportedOperationException();
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