package com.tripPortal.Observateur;

import com.tripPortal.Menu.AdminMenu;

import javafx.scene.Scene;

public class ClientDisplay implements Observer {

	private ClientStation subject;

    public ClientDisplay(Subject sub) {
		this.subject = (ClientStation) sub;
    }

	@Override
	public void update(String event) {
		AdminMenu adminMenu = subject.getAdminMenu();
		if (adminMenu == null) return;

		Scene currentScene = adminMenu.getCurrentScene();
		String currentPage = adminMenu.getCurrentPage();

		if (event.equals("seatReserved") && "tripList".equals(currentPage)) {
			adminMenu.displayTrips(currentScene, "A seat has been reserved on an existing trip");
		} else if (event.equals("reservationCancelled") && "tripList".equals(currentPage)) {
			adminMenu.displayTrips(currentScene, "A reservation has been cancelled on an existing trip");
		}
	}

	public void setSubject(Subject sub) {
		this.subject = (ClientStation) sub;
	}

}