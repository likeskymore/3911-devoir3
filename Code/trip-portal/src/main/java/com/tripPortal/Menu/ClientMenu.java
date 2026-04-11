package com.tripPortal.Menu;

import com.tripPortal.Model.Trip;
import com.tripPortal.Model.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClientMenu {

	public void start(Stage stage) {
		// TODO - implement com.tripPortal.Menu.ClientMenu.start
		Stage newStage = new Stage();

		VBox root = new VBox(new Label("Client Menu"));
		root.setSpacing(10);
		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root, 800, 600);

		newStage.setTitle("Client");
		newStage.setScene(scene);

		newStage.show();
	}
	/**
	 * 
	 * @param trip
	 */
	public void displaySearchTrips(ArrayList<Trip> trip) {
		// TODO - implement com.tripPortal.Menu.ClientMenu.displaySearchTrips
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 */
	public void displayReservationForm(Trip trip) {
		// TODO - implement com.tripPortal.Menu.ClientMenu.displayReservationForm
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 */
	public void displayEditProfile(User user) {
		// TODO - implement com.tripPortal.Menu.ClientMenu.displayEditProfile
		throw new UnsupportedOperationException();
	}

}