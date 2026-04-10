package com.tripPortal.Model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Route extends Trip {

	private ArrayList<TrainStation> trainRoute;
	private Train train;

	public Route(Company servicedBy, LocalTime departureTime, LocalTime arrivalTime,
				  float price, LocalTime tripDuration, ArrayList<TrainStation> trainRoute, Train train) {
		super(servicedBy,departureTime,arrivalTime,price,tripDuration);
		this.trainRoute = trainRoute;
		this.train = train;
	}
}