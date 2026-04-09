package com.tripPortal.Model;
import com.tripPortal.Etat.SeatState;

public abstract class Seat {

	private String id;
	private SeatState currentState;

	/**
	 * 
	 * @param s
	 */
	public void setState(SeatState s) {
		// TODO - implement com.tripPortal.Model.Seat.setState
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param e
	 */
	public void event(String e) {
		// TODO - implement com.tripPortal.Model.Seat.event
		throw new UnsupportedOperationException();
	}

}