package com.tripPortal.Factory;

import com.tripPortal.Model.Location;
import com.tripPortal.Model.Port;

public class PortFactory extends BoatTripFactory {

	/**
	 * 
	 * @param city
	 */
	public Location createLocation(String city) {
		// TODO - implement PortFactory.createLocation
		Location p = new Port(city);
		return p;
	}

}