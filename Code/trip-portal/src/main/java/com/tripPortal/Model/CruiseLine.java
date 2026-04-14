package com.tripPortal.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CruiseLine extends Trip {

	private ArrayList<Port> path;
	private Boat boat;

	public CruiseLine(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime,
			float price, int tripDuration, ArrayList<Port> path, Boat boat) {
		super(servicedBy, departureTime, arrivalTime, price, tripDuration);
		this.path = path;
		this.boat = boat;
	}

	public CruiseLine(String id) {
		super(id);
	}

	public CruiseLine(){
		super();
	}

}