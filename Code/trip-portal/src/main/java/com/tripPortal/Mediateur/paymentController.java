package com.tripPortal.Mediateur;

import com.tripPortal.Model.Reservation;

public abstract class paymentController {

	/**
	 * 
	 * @param name
	 * @param email
	 * @param passeportNumber
	 */
	public void VerifyInfo(String name, String email, String passeportNumber) {
		// TODO - implement paymentController.VerifyInfo
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param reservation
	 * @param cardNumber
	 */
	public boolean makePayment(Reservation reservation, String cardNumber) {
		// TODO - implement paymentController.makePayment
		throw new UnsupportedOperationException();
	}

}