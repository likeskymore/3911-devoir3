package com.tripPortal.Commande;

import com.tripPortal.Model.Trip;

public class editPriceCommand implements Command{

	String newPrice;
	Trip trip;

	public editPriceCommand(Trip trip, String newPrice){
		this.trip = trip;
		this.newPrice = newPrice;
	}

	public void execute() {
		trip.updatePrice(newPrice);
	}

	public void undo() {
		// TODO - implement editPriceCommand.undo
		throw new UnsupportedOperationException();
	}

}