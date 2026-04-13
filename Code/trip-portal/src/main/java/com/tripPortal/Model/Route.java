package com.tripPortal.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Route extends Trip {

	private ArrayList<TrainStation> trainRoute;
	private Train train;

	public Route(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime,
				  float price, int tripDuration, ArrayList<TrainStation> trainRoute, Train train) {
		super(servicedBy,departureTime,arrivalTime,price,tripDuration);
		this.trainRoute = trainRoute;
		this.train = train;
	}

	@Override
	public String getType() {
		return "Route"; 
	}

	@Override
	public String getDisplayCities() {
		return trainRoute.get(0).getCity() + " → " + trainRoute.get(trainRoute.size() - 1).getCity();
	}

	@Override
	public Transport getTransport() {
		return train;
	}

}