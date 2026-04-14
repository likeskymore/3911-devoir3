package com.tripPortal.Observateur;

import com.tripPortal.Menu.AdminMenu;
import com.tripPortal.Menu.ClientMenu;

import javafx.scene.Scene;

public class ClientDisplay implements Observer {

	private ClientStation subject;

    public ClientDisplay(Subject sub) {
		this.subject = (ClientStation) sub;
    }

    @Override
    public void update(String event) {
        AdminMenu adminMenu = subject.getAdminMenu();
		Scene currentScene = adminMenu.getCurrentScene();
		if (event.equals("seatReserved") && adminMenu.getCurrentPage().equals("tripList")) {
            adminMenu.displayTrips(currentScene, "A seat has been reserved on an existing trip");
		} else if (event.equals("reservationCancelled") && adminMenu.getCurrentPage().equals("tripList")) {
            adminMenu.displayTrips(currentScene, "A reservation has been cancelled on an existing trip");
		}
    }

	public void setSubject(Subject sub) {
		this.subject = (ClientStation) sub;
	}

}