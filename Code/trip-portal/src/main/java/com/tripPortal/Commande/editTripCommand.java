package com.tripPortal.Commande;

import com.tripPortal.Model.Trip;

public class editTripCommand {

	private Trip trip;
	String newName;

	public editTripCommand(Trip trip, String newName){
		this.trip = trip;
		this.newName = newName;
	}

	public void execute() {
		// TODO - implement editTripCommand.execute
		throw new UnsupportedOperationException();
	}

	public void undo() {
		// TODO - implement editTripCommand.undo
		throw new UnsupportedOperationException();
	}

}