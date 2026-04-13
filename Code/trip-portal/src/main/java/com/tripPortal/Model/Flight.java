package com.tripPortal.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Flight extends Trip {

	private Airport departure;
	private Airport destination;
	private Plane plane;

	public Flight(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime,
			float price, int tripDuration, Airport departure, Airport destination, Plane plane) {
		super(servicedBy, departureTime, arrivalTime, price, tripDuration);
		this.departure = departure;
		this.destination = destination;
		this.plane = plane;
	}

	public Flight(String id) {
		super(id);
	}
}