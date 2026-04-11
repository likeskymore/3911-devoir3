package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.Location;

public class TrainCompanyFactory extends TrainTripFactory {

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