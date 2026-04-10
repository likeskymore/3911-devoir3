package com.tripPortal.Model;

import java.time.LocalTime;
import java.util.ArrayList;

public class CruiseLine extends Trip {

	private ArrayList<Port> path;
	private Boat boat;

	public CruiseLine(Company servicedBy, LocalTime departureTime, LocalTime arrivalTime,
				 float price, LocalTime tripDuration, ArrayList<Port> path, Boat boat) {
		super(servicedBy,departureTime,arrivalTime,price,tripDuration);
		this.path = path;
		this.boat = boat;
	}

}