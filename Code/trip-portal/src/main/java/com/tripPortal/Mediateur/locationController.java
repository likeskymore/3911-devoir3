package com.tripPortal.Mediateur;

import com.tripPortal.Factory.AirportFactory;
import com.tripPortal.Factory.PortFactory;
import com.tripPortal.Factory.TrainStationFactory;
import com.tripPortal.Model.Location;

public class locationController {

	public void goCallCreateLocation(String city, String type) {

		if (type == "Airport") {
			AirportFactory.getInstance().createLocation(city);
		} else if (type == "Port") {
			PortFactory.getInstance().createLocation(city);
		} else if (type == "Station") {
			TrainStationFactory.getInstance().createLocation(city);
		} else {
			System.err.println("Unknown location type: " + type);
		}
		
	}

	/**
	 * 
	 * @param location
	 * @param city
	 */
	public void editCity(Location location, String city) {
		// TODO - implement locationController.editCity
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param location
	 */
	public void deleteLocation(Location location) {
		// TODO - implement locationController.deleteLocation
		throw new UnsupportedOperationException();
	}

}