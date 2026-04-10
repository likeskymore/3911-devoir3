package com.tripPortal.Factory;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.Location;

public class FlightCompanyFactory extends PlaneTripFactory {

	public Location createLocation(String city){
		return null;
	}
	public Company createCompany(String name) {
		// TODO - implement FlightCompanyFactory.createCompany
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