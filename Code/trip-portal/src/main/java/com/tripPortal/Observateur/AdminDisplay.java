package com.tripPortal.Observateur;


public class AdminDisplay implements Observer {

	private Subject subject;

    public void update(String event, Object data) {
        System.out.println("Admin notified: trip list modification");
    }

	public void setSubject(Subject sub) {
		this.subject = sub;
	}
}