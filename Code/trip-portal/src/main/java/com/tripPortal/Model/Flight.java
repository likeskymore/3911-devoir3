package com.tripPortal.Model;

import java.time.LocalTime;

public class Flight extends Trip {

	private Airport departure;
	private Airport destination;
	private Plane plane;

	public Flight(Company servicedBy, LocalTime departureTime, LocalTime arrivalTime,
				  float price, LocalTime tripDuration, Airport departure, Airport destination, Plane plane) {
		super(servicedBy,departureTime,arrivalTime,price,tripDuration);
		this.departure = departure;
		this.destination = destination;
		this.plane = plane;
	}
}