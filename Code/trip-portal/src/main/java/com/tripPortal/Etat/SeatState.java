package com.tripPortal.Etat;
import com.tripPortal.Model.Seat;

public interface SeatState {

	/**
	 * 
	 * @param context
	 */
	void next(Seat context);

	/**
	 * 
	 * @param context
	 */	
	void cancel(Seat context);


}