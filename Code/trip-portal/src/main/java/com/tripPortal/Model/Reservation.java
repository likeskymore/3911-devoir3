package com.tripPortal.Model;

import java.time.LocalTime;
import java.util.Random;

public class Reservation {

	private String reservationNumber;
	private LocalTime startTimeReservation;
	private String transportID;
	private String reservedSeat;
	private String tripId;
	private boolean isPaid;

	public Reservation(String transportID, String reservedSeat, String tripId, boolean isPaid) {
		this.reservationNumber = generateReservationNumber();
		this.startTimeReservation = LocalTime.now();
		this.transportID = transportID;
		this.reservedSeat = reservedSeat;
		this.tripId = tripId;
		this.isPaid = isPaid;
	}

	public String generateReservationNumber() {
        String id = "";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int number = (rand.nextInt(9));
            id += number;
        }
        return id;
    }

	public String getReservationNumber() {
		return reservationNumber;
	}

	public LocalTime getStartTimeReservation() {
		return startTimeReservation;
	}

	public String getTransportID() {
		return transportID;
	}

	public String getReservedSeat() {
		return reservedSeat;
	}

	public String getTripId() {
		return tripId;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}	

}