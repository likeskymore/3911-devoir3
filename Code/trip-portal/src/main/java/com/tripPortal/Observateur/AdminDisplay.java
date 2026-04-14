package com.tripPortal.Observateur;

import com.tripPortal.Menu.ClientMenu;

import javafx.scene.Scene;

public class AdminDisplay implements Observer {

	private AdminStation subject;

    public AdminDisplay(Subject sub) {
		this.subject = (AdminStation) sub;
    }

	@Override
	public void update(String event) {
		ClientMenu clientMenu = subject.getClientMenu();
		if (clientMenu == null) return;

		Scene currentScene = clientMenu.getCurrentScene();
		String currentPage = clientMenu.getCurrentPage();

		if (event.equals("tripUpdated") && "reserve".equals(currentPage)) {
			clientMenu.displayReserveMenu(currentScene, "An existing trip has been updated");
		} else if (event.equals("tripDeleted") && "reserve".equals(currentPage)) {
			clientMenu.displayReserveMenu(currentScene, "A trip has been deleted");
		} else if (event.equals("tripCreated") && "reserve".equals(currentPage)) {
			clientMenu.displayReserveMenu(currentScene, "A new trip has been created");
		}
	}

	public void setSubject(Subject sub) {
		this.subject = (AdminStation) sub;
	}

}