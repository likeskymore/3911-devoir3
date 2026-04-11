package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class TrainStationFactory extends TrainTripFactory {

	/**
	 * 
	 * @param city
	 */
	public Location createLocation(String city) {
		// TODO - implement TrainStationFactory.createLocation
		Location t = new TrainStation(city);
		return t;
	}
    public Company createCompany(String name) {
        // TODO - implement TrainStationFactory.createCompany
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