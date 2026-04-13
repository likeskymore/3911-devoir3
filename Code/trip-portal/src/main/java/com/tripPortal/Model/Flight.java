package com.tripPortal.Model;

import java.time.LocalDate;
public class Flight extends Trip {

	private Airport departure;
	private Airport destination;
	private Plane plane;

	public Flight(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime,
				  float price, int tripDuration, Airport departure, Airport destination, Plane plane) {
		super(servicedBy,departureTime,arrivalTime,price,tripDuration);
		this.departure = departure;
		this.destination = destination;
		this.plane = plane;
	}

	public Airport getDeparture() {
		return departure;
	}

	public Airport getDestination() {
		return destination;
	}

	@Override
	public String getType() {
		return "Flight";
	}

	@Override
	public String getDisplayCities() {
		return departure.getCity() + " → " + destination.getCity();
	}

	@Override
	public Transport getTransport() {
		return plane;
	}
}