package com.tripPortal.Observateur;

public class ClientDisplay implements Observer {

	private Subject subject;

    public void update(String event, Object data) {
        if (event.equals("TRIP_ADDED")) {
            // table is already bound to ObservableList
            // nothing to do manually — binding handles it
            System.out.println("Client notified: new trip available");
        }
    }

	public void setSubject(Subject sub) {
		this.subject = sub;
	}

}