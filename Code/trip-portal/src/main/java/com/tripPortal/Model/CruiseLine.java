package com.tripPortal.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CruiseLine extends Trip {

	private ArrayList<Port> path;
	private Boat boat;

	public CruiseLine(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime,
			float price, int tripDuration, ArrayList<Port> path, Boat boat) {
		super(servicedBy, departureTime, arrivalTime, price, tripDuration);
		this.path = path;
		this.boat = boat;
	}
	@Override
	public String getType() {
		return "CruiseLine"; 
	}

	@Override
	public String getDisplayCities() {
		return path.get(0).getCity() + " → " + path.get(path.size() - 1).getCity();
	}

	@Override
	public Transport getTransport() {
		return boat;
	}

	public CruiseLine(String id) {
		super(id);
	}

}