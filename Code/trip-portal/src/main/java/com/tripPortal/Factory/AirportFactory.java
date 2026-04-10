package com.tripPortal.Factory;

import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Location;

public class AirportFactory extends PlaneTripFactory {

	/**
	 * 
	 * @param city
	 */
	public Location createLocation(String city) {
		// TODO - implement AirportFactory.createLocation
		Location l = new Airport( city );
		return l;
	}

}