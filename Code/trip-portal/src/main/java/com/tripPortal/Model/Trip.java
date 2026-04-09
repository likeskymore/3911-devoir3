package com.tripPortal.Model;

import java.time.LocalTime;

public abstract class Trip {

	private String id;
	private Company servicedBy;
	private LocalTime departureTime;
	private LocalTime arrivalTime;
	private float price;
	private LocalTime tripDuration;

}