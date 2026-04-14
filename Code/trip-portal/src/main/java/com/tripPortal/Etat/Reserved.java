package com.tripPortal.Etat;

import com.tripPortal.Model.Seat;

public class Reserved implements SeatState {

	public Reserved() {
	}

	/**
	 * 
	 * @param context
	 */
	public void next(Seat context) {
		context.setState(new Occupied());
	}

	/**
	 * 
	 * @param context
	 */
	public void cancel(Seat context) {
		context.setState(new Available());
	}
}