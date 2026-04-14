package com.tripPortal.Observateur;

public class ClientDisplay implements Observer {

	private ClientStation subject;

    public ClientDisplay(Subject sub) {
		this.subject = (ClientStation) sub;
    }

    @Override
    public void update(String event) {
        subject.getAdminMenu().displayMenuAdmin(null);
    }

	public void setSubject(Subject sub) {
		this.subject = (ClientStation) sub;
	}

}