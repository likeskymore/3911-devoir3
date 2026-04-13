package com.tripPortal.Etat;

import com.tripPortal.Model.Seat;

public class Occupied implements SeatState {

	public Occupied() {
	}

	/**
	 * 
	 * @param context
	 */
	public void next(Seat context) {
		return;
	}

	/**
	 * @param context
	 */
	public void cancel(Seat context) {
		context.setState(new Available());
		return;
	}

}