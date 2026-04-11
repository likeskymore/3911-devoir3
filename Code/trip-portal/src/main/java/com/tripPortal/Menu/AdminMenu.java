package com.tripPortal.Menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import com.tripPortal.Factory.RouteFactory;
import com.tripPortal.Factory.TripFactory;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.BoatCompany;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.CruiseLine;
import com.tripPortal.Model.FlightCompany;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.TrainCompany;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class AdminMenu {
	private TripFactory tripFactory;
	private RouteFactory routeFactory;
	private CruiseLine cruiseLine;


	public void start(Stage stage) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.start
		Stage newStage = new Stage();

		Scene scene = new Scene(new VBox(), 600, 400);
		displayMenuAdmin(scene);

		newStage.setTitle("Admin");
		newStage.setScene(scene);

		newStage.show();
	}

	public void displayMenuAdmin(Scene scene) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayMenuAdmin
		VBox root = new VBox();
		Button	editProfileButton = new Button("Profile");
		editProfileButton.setMinWidth(200);
		editProfileButton.setPrefHeight(50);
		Button	tripsButton = new Button("Trips");
		tripsButton.setMinWidth(200);
		tripsButton.setPrefHeight(50);
		tripsButton.setOnAction(e -> {
			displayTripsMenu(scene);
		});
		Button	companiesButton = new Button("Companies");
		companiesButton.setMinWidth(200);
		companiesButton.setPrefHeight(50);
		Button	locationsButton = new Button("Locations");
		locationsButton.setMinWidth(200);
		locationsButton.setPrefHeight(50);
		Button	transportsButton = new Button("Transports");
		transportsButton.setMinWidth(200);
		transportsButton.setPrefHeight(50);
		Button  logoutButton = new Button("Logout");
		logoutButton.setMinWidth(200);
		logoutButton.setPrefHeight(50);
		root.getChildren().addAll(editProfileButton, tripsButton, companiesButton, locationsButton, transportsButton, logoutButton);
		BorderPane borderPane = new BorderPane();
		borderPane.setLeft(root);
		scene.setRoot(borderPane);
	}

	void displayTripsMenu(Scene scene) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayTripsMenu
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> {
			displayMenuAdmin(scene);
		});

		VBox back = new VBox(backButton);
		VBox pageContent = new VBox();
		Button CreateTripButton = new Button("Create Trip");
		CreateTripButton.setMinWidth(50);
		CreateTripButton.setPrefHeight(50);
		CreateTripButton.setOnAction(e -> {
			displayTripCreationForm(scene);
		});
		ArrayList<Trip> trips = new ArrayList<>();

		// Button EditTripButton = new Button("Edit Trip");
		// EditTripButton.setMinWidth(200);
		// EditTripButton.setPrefHeight(50);
		// Button DeleteTripButton = new Button("Delete Trip");
		// DeleteTripButton.setMinWidth(100);
		// DeleteTripButton.setPrefHeight(50);
		pageContent.getChildren().addAll(CreateTripButton);
		pageContent.setAlignment(Pos.CENTER);
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);
		HBox.setHgrow(pageContent, Priority.ALWAYS);
		HBox layout = new HBox(back, pageContent);

		scene.setRoot(layout);
	}

	public void displayTripCreationForm(Scene scene) {

		HBox layout = new HBox();

		// ---------------- BACK BUTTON ----------------
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> displayTripsMenu(scene));

		VBox back = new VBox(backButton);

		// ---------------- FORM ----------------
		VBox form = new VBox(10);
		form.setAlignment(Pos.CENTER);

		CheckBox flightCheckBox = new CheckBox("Flight");
		CheckBox boatCheckBox = new CheckBox("Cruise");
		CheckBox trainCheckBox = new CheckBox("Route");
		HBox checkboxes = new HBox(5, flightCheckBox, boatCheckBox, trainCheckBox);

		Label companyLabel = new Label("Select Company:");
		ComboBox<String> CompanyComboBox = new ComboBox<>();
		VBox companyBox = new VBox(5, companyLabel, CompanyComboBox);

		Label locationLabel = new Label("Select Location:");
		ComboBox<String> LocationComboBox = new ComboBox<>();
		VBox locationBox = new VBox(5, locationLabel, LocationComboBox);

		
		Label transportLabel = new Label("Select Transport:");
		ComboBox<String> TransportComboBox = new ComboBox<>();
		VBox transportBox = new VBox(5, transportLabel, TransportComboBox);

		DatePicker startDate = new DatePicker();
		DatePicker endDate = new DatePicker();

		TextField priceField = new TextField();
		priceField.setPromptText("Price");

		Button submitButton = new Button("Submit");
		submitButton.setMinWidth(100);
		submitButton.setPrefHeight(20);
		submitButton.setOnAction(e -> {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root1 = mapper.readTree(new File("src/Database/Company.json"));
				JsonNode root2 = mapper.readTree(new File("src/Database/Location.json"));
				JsonNode root3 = mapper.readTree(new File("src/Database/Transport.json"));

				Company company      = Company.fromJson(CompanyComboBox.getValue(), root1);
				Location origin      = Location.fromJson(LocationComboBox.getValue(), root2);
				Location destination = Location.fromJson(LocationComboBox.getValue(), root2);
				Transport transport  = Transport.fromJson(TransportComboBox.getValue(), root3);

				TripFactory factory = TripFactory.getFactory(
					flightCheckBox.isSelected() ? "Flight" :
					boatCheckBox.isSelected()   ? "Cruise" :
					trainCheckBox.isSelected()  ? "Train"  : ""
				);

				factory.createTrajectory(
					company,
					startDate.getValue(),
					endDate.getValue(),
					Float.parseFloat(priceField.getText()),
					(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
					new ArrayList<>(),
					transport
				);

			} catch (IOException ex) {
				System.err.println("Failed to read JSON files: " + ex.getMessage());
				ex.printStackTrace();
			} catch (IllegalArgumentException ex) {
				System.err.println("Invalid selection: " + ex.getMessage());
				ex.printStackTrace();
			}
		});

		// ---------------- LOAD JSON ONCE ----------------
		ObjectMapper mapper = new ObjectMapper();
		File companyFile = new File("src/Database/Company.json");
		File locationFile = new File("src/Database/Location.json");
		File transportFile = new File("src/Database/Transport.json");

		JsonNode temp1, temp2, temp3;
		try {
			temp1 = mapper.readTree(companyFile);
			temp2 = mapper.readTree(locationFile);
			temp3 = mapper.readTree(transportFile);
		} catch (Exception e) {
			e.printStackTrace();
			temp1 = mapper.createArrayNode();
			temp2 = mapper.createArrayNode();
			temp3 = mapper.createArrayNode();
		}

		final JsonNode root1 = temp1;
		final JsonNode root2 = temp2;
		final JsonNode root3 = temp3;

		// ---------------- GUARD FLAG ----------------
		final boolean[] updating = {false};

		// ---------------- LOGIC ----------------
		flightCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (updating[0]) return;
			if (newVal) {
				updating[0] = true;
				boatCheckBox.setSelected(false);
				trainCheckBox.setSelected(false);
				updating[0] = false;
				updateCompanies(root1, CompanyComboBox, "FlightCompany");
				updateLocations(root2, LocationComboBox, "Airport");
				updateTransports(root3, TransportComboBox, "Plane");
			}
		});

		boatCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (updating[0]) return;
			if (newVal) {
				updating[0] = true;
				flightCheckBox.setSelected(false);
				trainCheckBox.setSelected(false);
				updating[0] = false;
				updateCompanies(root1, CompanyComboBox, "CruiseCompany");
				updateLocations(root2, LocationComboBox, "Port");
				updateTransports(root3, TransportComboBox, "Boat");
			}
		});

		trainCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (updating[0]) return;
			if (newVal) {
				updating[0] = true;
				flightCheckBox.setSelected(false);
				boatCheckBox.setSelected(false);
				updating[0] = false;
				updateCompanies(root1, CompanyComboBox, "TrainCompany");
				updateLocations(root2, LocationComboBox, "TrainStation");
				updateTransports(root3, TransportComboBox, "Train");
			}
		});

		// ---------------- ADD COMPONENTS ----------------
		form.getChildren().addAll(
				checkboxes,
				companyBox,
				locationBox,
				transportBox,
				startDate,
				endDate,
				submitButton
		);

		// ---------------- LAYOUT ----------------
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);

		HBox.setHgrow(form, Priority.ALWAYS);

		layout.getChildren().addAll(back, form);
		scene.setRoot(layout);
	}

	private void updateCompanies(JsonNode root, ComboBox<String> comboBox, String type) {
		comboBox.getItems().clear();

		for (JsonNode node : root) {
			if (node.has("type") && node.get("type").asText().equals(type)) {
				comboBox.getItems().add(node.get("name").asText());
			}
		}
	}

	private void updateLocations(JsonNode root, ComboBox<String> comboBox, String type) {
		comboBox.getItems().clear();

		for (JsonNode node : root) {
			if (node.has("type") && node.get("type").asText().equals(type)) {
				comboBox.getItems().add(node.get("name").asText());
			}
		}
	}

	private void updateTransports(JsonNode root, ComboBox<String> comboBox, String type) {
		comboBox.getItems().clear();

		for (JsonNode node : root) {
			if (node.has("type") && node.get("type").asText().equals(type)) {
				comboBox.getItems().add(node.get("name").asText());
			}
		}
	}

	/**
	 * 
	 * @param user
	 */
	public void displayEditProfile(User user) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayEditProfile
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param companies
	 */
	public void displayCompanies(ArrayList<Company> companies) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayCompanies
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trips
	 */
	public void displayTrips(ArrayList<Trip> trips) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayTrips
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param location
	 */
	public void displayLocations(ArrayList<Location> location) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayLocations
		throw new UnsupportedOperationException();
	}

}