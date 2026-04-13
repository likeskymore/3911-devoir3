package com.tripPortal.Commande;

import java.time.LocalDate;

import com.tripPortal.Model.Trip;

public class editTripCommand implements Command {

	private Trip trip;
	private String newCompany;
	private LocalDate newStartDate;
	private LocalDate newEndDate;
	private float newPrice;
	private String newTransportId;

	public editTripCommand(Trip trip, String newCompany, LocalDate newStartDate, LocalDate newEndDate,
			float newPrice, String newTransportId){
		this.trip = trip;
		this.newCompany = newCompany;
		this.newStartDate = newStartDate;
		this.newEndDate = newEndDate;
		this.newPrice = newPrice;
		this.newTransportId = newTransportId;
	}

	public void execute() {
		trip.updateTrip(newCompany, newStartDate, newEndDate, newPrice, newTransportId);
	}

	public void undo() {
		throw new UnsupportedOperationException();
	}

}