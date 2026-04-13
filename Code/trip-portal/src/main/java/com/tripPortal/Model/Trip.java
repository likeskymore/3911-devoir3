package com.tripPortal.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import com.tripPortal.Factory.TripFactory;

public abstract class Trip {

	private String id;
	private Company servicedBy;
	private LocalDate departureTime;
	private LocalDate arrivalTime;
	private float price;
	private int tripDuration;
	private TripFactory tripFactory;
	private Boolean active;

	public Trip(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime, float price, int tripDuration) {
		this.id = randomGenerateID(servicedBy);
		this.servicedBy = servicedBy;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
		this.tripDuration = tripDuration;
		this.active = true;
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
	public String getId() {
		return id;
	}

	public Company getServicedBy() {
		return servicedBy;
	}

	public LocalDate getDepartureTime() {
		return departureTime;
	}

	public LocalDate getArrivalTime() {
		return arrivalTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getTripDuration() {
		return tripDuration;
	}

	public abstract String getType();
	public abstract String getDisplayCities();
	public abstract Transport getTransport();



}