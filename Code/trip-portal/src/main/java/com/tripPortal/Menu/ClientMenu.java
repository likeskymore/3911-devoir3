package com.tripPortal.Menu;

import java.util.ArrayList;

import com.tripPortal.Mediateur.tripController;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.User;
import com.tripPortal.Visiteur.ConcreteCruiseLineVisitor;
import com.tripPortal.Visiteur.ConcreteFlightVisitor;
import com.tripPortal.Visiteur.ConcreteRouteVisitor;
import com.tripPortal.Visiteur.ListTripsDataStructure;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMenu {

	private tripController tripControllerForClientMenu;

	public void start(Stage stage) {
		// TODO - implement com.tripPortal.Menu.ClientMenu.start
		Stage newStage = new Stage();


		Scene scene = new Scene(new VBox(), 600, 400);
		displayMenuClient(scene);


		newStage.setTitle("Client");
		newStage.setScene(scene);

		newStage.show();
	}
	
	public void displayMenuClient(Scene scene) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayMenuAdmin
		VBox root = new VBox();
		Button editProfileButton = new Button("Profile");
		editProfileButton.setMinWidth(200);
		editProfileButton.setPrefHeight(50);

		Button tripsButton = new Button("Reserve a trip");
		tripsButton.setMinWidth(200);
		tripsButton.setPrefHeight(50);
		tripsButton.setOnAction(e -> {
			displayReserveMenu(scene, "");
		});

		Button logoutButton = new Button("Logout");
		logoutButton.setMinWidth(200);
		logoutButton.setPrefHeight(50);
		logoutButton.setOnAction(e -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES,
					ButtonType.NO);
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					scene.getWindow().hide();
				}
			});
		});

		root.getChildren().addAll(editProfileButton, tripsButton, logoutButton);

		BorderPane borderPane = new BorderPane();
		borderPane.setLeft(root);
		scene.setRoot(borderPane);
	}

	public void displayReserveMenu(Scene scene, String message) {
		ListTripsDataStructure structure = tripControllerForClientMenu.fetchAllTripsAsStructure();

		ArrayList<String> lines = new ArrayList<>();
		lines.addAll(structure.accept(new ConcreteFlightVisitor()));
		lines.addAll(structure.accept(new ConcreteRouteVisitor()));
		lines.addAll(structure.accept(new ConcreteCruiseLineVisitor()));

		// Filtre les lignes vides
		lines.removeIf(String::isBlank);

		// ── Liste des trips ───────────────────────────────────────────
		VBox tripList = new VBox(8);
		tripList.setPadding(new Insets(10));
		for (String line : lines) {
			Label label = new Label(line);
			label.setWrapText(true);
			label.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 8;");
			label.setPrefWidth(300);
			Button reserveButton = new Button("Reserve");
			reserveButton.setOnAction(e -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reservation successful for:\n" + line, ButtonType.OK);
				alert.showAndWait();
			});
			tripList.getChildren().add(new HBox(10, label, reserveButton));
		}

		ScrollPane scroll = new ScrollPane(tripList);
		scroll.setFitToWidth(true);
		scroll.setPrefHeight(350);

		// ── Message si applicable ─────────────────────────────────────
		VBox content = new VBox(10);
		content.setPadding(new Insets(20));
		if (message != null && !message.isBlank()) {
			Label msgLabel = new Label(message);
			msgLabel.setStyle("-fx-text-fill: green;");
			content.getChildren().add(msgLabel);
		}
		content.getChildren().add(scroll);

		// ── Bouton Back ───────────────────────────────────────────────
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> displayMenuClient(scene));

		VBox back = new VBox(backButton);
		back.setPrefWidth(150);
		back.setMinWidth(150);
		back.setMaxWidth(150);

		HBox.setHgrow(content, Priority.ALWAYS);
		HBox layout = new HBox(back, content);
		scene.setRoot(layout);
	}

	public void setTripControllerForClientMenu(tripController tripControllerForClientMenu) {
		this.tripControllerForClientMenu = tripControllerForClientMenu;
	}


}