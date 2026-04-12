package com.tripPortal.Menu;

import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPortal.Mediateur.reservationController;
import com.tripPortal.Mediateur.tripController;
import com.tripPortal.Model.Reservation;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMenu {

	private tripController tripControllerForClientMenu;
	private reservationController reservationControllerForClientMenu;

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
		editProfileButton.setOnAction(e -> {
			displayProfileMenu(scene);
		});

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

	public void displayProfileMenu(Scene scene) {
		HBox layout = new HBox(10);

		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> {
			displayMenuClient(scene);
		});

		VBox back = new VBox(backButton);
		back.setPrefWidth(150);
		back.setMinWidth(150);
		back.setMaxWidth(150);

		VBox content = new VBox(10);
		content.setPadding(new Insets(20));
		content.getChildren().add(new Label("Your Reservations:"));

		ArrayList<Reservation> reservations = reservationControllerForClientMenu.fetchUserReservations();

		VBox reservationList = new VBox(8);
		reservationList.setPadding(new Insets(10));

		for (Reservation res : reservations) {
			String line = "Reservation #" + res.getReservationNumber()
					+ " - Trip ID: " + res.getTripId()
					+ " - Seat: " + res.getReservedSeat()
					+ " - Paid: " + (res.isPaid() ? "Yes" : "No");

			Label resLabel = new Label(line);
			resLabel.setWrapText(true);
			resLabel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 8;");
			resLabel.setPrefWidth(300);

			VBox buttons = new VBox(5);
			Button payButton = new Button("Pay");
			payButton.setDisable(res.isPaid()); // Disable if already paid
			payButton.setOnAction(e -> {
				reservationControllerForClientMenu.payReservation(res);
				displayProfileMenu(scene);
			});
			payButton.setMinWidth(80);

			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e -> {
				reservationControllerForClientMenu.cancelReservation(res);
				displayProfileMenu(scene); // Refresh after deletion
			});
			cancelButton.setMinWidth(80);

			buttons.getChildren().addAll(payButton, cancelButton);

			reservationList.getChildren().add(new HBox(10, resLabel, buttons));
		}

		ScrollPane scroll = new ScrollPane(reservationList);
		scroll.setFitToWidth(true);
		scroll.setPrefHeight(350);

		content.getChildren().add(scroll);

		HBox.setHgrow(content, Priority.ALWAYS);
		layout.getChildren().addAll(back, content);
		scene.setRoot(layout);
	}

	public void displayReserveMenu(Scene scene, String message) {
		ListTripsDataStructure structure = tripControllerForClientMenu.fetchAllTripsAsStructure();

		// ── Collecter paires (affichage, node) ────────────────────────
		ArrayList<javafx.util.Pair<String, JsonNode>> pairs = new ArrayList<>();
		pairs.addAll(structure.acceptWithNodes(new ConcreteFlightVisitor()));
		pairs.addAll(structure.acceptWithNodes(new ConcreteRouteVisitor()));
		pairs.addAll(structure.acceptWithNodes(new ConcreteCruiseLineVisitor()));

		// ── Liste des trips ───────────────────────────────────────────
		VBox tripList = new VBox(8);
		tripList.setPadding(new Insets(10));
		for (javafx.util.Pair<String, JsonNode> pair : pairs) {
			String line   = pair.getKey();
			JsonNode node = pair.getValue();

			Label label = new Label(line);
			label.setWrapText(true);
			label.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 8;");
			label.setPrefWidth(300);

			Button reserveButton = new Button("Reserve");
			reserveButton.setOnAction(e -> {
				displayAvailableSeats(scene, node);
			});
			reserveButton.setMinWidth(80);

			tripList.getChildren().add(new HBox(10, label, reserveButton));
		}

		ScrollPane scroll = new ScrollPane(tripList);
		scroll.setFitToWidth(true);
		scroll.setPrefHeight(350);

		// ── Message si applicable ─────────────────────────────────────
		VBox content = new VBox(10);
		Label header = new Label("Available Trips:");
		content.getChildren().add(header);
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

	public void displayAvailableSeats(Scene scene, JsonNode tripNode) {
		JsonNode transport = tripNode.get("transport");
		String type = tripNode.get("type").asText();
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("src/Database/Transport.json");
		
		try {
			JsonNode root = mapper.readTree(file);
			if (root == null || !root.isArray()) return;

			ListView<String> listView = new ListView<>();
			if(type.equals("CruiseLine")) {
				for (JsonNode node : root) {
					if (node.has("transportID") && node.get("transportID").asText().equals(transport.asText())) {
						for (JsonNode section : node.get("sections")) {
							for (JsonNode cabin : section.get("cabins")) {
								if (cabin.get("occupied").asBoolean() == false) {
									listView.getItems().add("Cabin ID: " + cabin.get("seatID").asText());
								}
							}
						}
					}
				}
			} else {
				for (JsonNode node : root) {
					if (node.has("transportID") && node.get("transportID").asText().equals(transport.asText())) {
						for (JsonNode Section : node.get("sections")) {
							for (JsonNode seat : Section.get("seats")) {
								if (seat.get("occupied").asBoolean() == false) {
									listView.getItems().add("Seat ID: " + seat.get("seatID").asText());
								}
							}
						}
					}
				}
			}
			VBox layout = new VBox(10);
			layout.setPadding(new Insets(20));	
			layout.getChildren().add(new Label("Available Seats:"));
			layout.getChildren().add(listView); // Placeholder for seat list
			Button backButton = new Button("Back");
			backButton.setOnAction(e -> displayReserveMenu(scene, ""));
			Button reserveButton = new Button("Reserve now");
			reserveButton.setOnAction(e -> {
				String selected = listView.getSelectionModel().getSelectedItem();
				if (selected == null) {
					new Alert(Alert.AlertType.WARNING, "Please select a seat.").showAndWait();
					return;
				}
				String seatID = selected.split(": ")[1];
				reservationControllerForClientMenu.reserveTrip(tripNode, seatID);
				displayReserveMenu(scene, "Reservation successful for seat: " + seatID);
			});
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			HBox buttons = new HBox(10, backButton, spacer, reserveButton);
			layout.getChildren().add(buttons);
			scene.setRoot(layout);

		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load transport data");
			alert.showAndWait();
		}

		
	}

	public void setTripControllerForClientMenu(tripController tripControllerForClientMenu) {
		this.tripControllerForClientMenu = tripControllerForClientMenu;
	}

	public void setReservationControllerForClientMenu(reservationController reservationControllerForClientMenu) {
		this.reservationControllerForClientMenu = reservationControllerForClientMenu;
	}


}