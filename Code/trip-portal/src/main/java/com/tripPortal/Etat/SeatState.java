package com.tripPortal.Etat;
import com.tripPortal.Model.Seat;

public interface SeatState {

	/**
	 * 
	 * @param context
	 * @param e
	 */
	void event(Seat context, String e);

}