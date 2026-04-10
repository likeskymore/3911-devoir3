package com.tripPortal.Factory;

import com.tripPortal.Model.Location;
import com.tripPortal.Model.TrainStation;

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

}