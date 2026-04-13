package com.tripPortal.Etat;

import com.tripPortal.Model.Seat;

public class Available implements SeatState {

	public Available() {
	}

	/**
	 * @param context
	 */
	public void next(Seat context) {
		context.setState(new Reserved());
	}

	/**
	 * @param context
	 */
	public void cancel(Seat context) {
		// No action needed, already available
		return;
	}

}