package com.tripPortal.Model;

import java.time.LocalTime;
import java.util.Random;

public abstract class Trip {

	private String id;
	private Company servicedBy;
	private LocalTime departureTime;
	private LocalTime arrivalTime;
	private float price;
	private LocalTime tripDuration;

	public Trip(Company servicedBy, LocalTime departureTime, LocalTime arrivalTime, float price, LocalTime tripDuration) {
		this.id = randomGenerateID(servicedBy);
		this.servicedBy = servicedBy;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
		this.tripDuration = tripDuration;
	}

	private String randomGenerateID(Company servicedBy) {
		String id = servicedBy.getTripID();
		Random rand = new Random();

		for (int i = 0; i < 4; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// complete verification ...

		return id;

	}
}