package com.tripPortal.Menu;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPortal.Factory.CruiseLineFactory;
import com.tripPortal.Factory.FlightFactory;
import com.tripPortal.Factory.RouteFactory;
import com.tripPortal.Mediateur.companyController;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Model.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu {
	private companyController CompanyControllerForAdminMenu;


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
		companiesButton.setOnAction(e -> {
			displayCompaniesMenu(scene);
		});
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

		Label locationLabel = new Label("Select Path (hold Ctrl to multi-select):");
		ListView<String> LocationListView = new ListView<>();
		LocationListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LocationListView.setPrefHeight(120);
		LocationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (flightCheckBox.isSelected()) {
				List<String> selected = LocationListView.getSelectionModel().getSelectedItems();
				if (selected.size() >= 2) {
					LocationListView.setCellFactory(lv -> new ListCell<String>() {
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							setText(empty ? null : item);
							// Disable unselected cells once limit is reached
							setDisable(!empty && !selected.contains(item));
							setOpacity(!empty && !selected.contains(item) ? 0.4 : 1.0);
						}
					});
				} else {
					// Re-enable all cells when under the limit
					LocationListView.setCellFactory(lv -> new ListCell<String>() {
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							setText(empty ? null : item);
							setDisable(false);
							setOpacity(1.0);
						}
					});
        		}
    		}
		});
		VBox locationBox = new VBox(5, locationLabel, LocationListView);

		
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

				Company company = Company.fromJson(CompanyComboBox.getValue(), root1);

				ArrayList<Location> locations = new ArrayList<>();
				for (String selectedName : LocationListView.getSelectionModel().getSelectedItems()) {
					Location loc = Location.fromJson(selectedName, root2);
					locations.add(loc);
				}

				Transport transport = Transport.fromJson(TransportComboBox.getValue(), root3);

				Trip trip;
				if (flightCheckBox.isSelected()) {
					trip = FlightFactory.getInstance().createTrajectory(
						company,
						startDate.getValue(),
						endDate.getValue(),
						Float.parseFloat(priceField.getText()),
						(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
						locations,
						transport
					);
				} else if (boatCheckBox.isSelected()) {
					trip = CruiseLineFactory.getInstance().createTrajectory(
						company,
						startDate.getValue(),
						endDate.getValue(),
						Float.parseFloat(priceField.getText()),
						(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
						locations,
						transport
					);
				} else if (trainCheckBox.isSelected()) {
					trip = RouteFactory.getInstance().createTrajectory(
						company,
						startDate.getValue(),
						endDate.getValue(),
						Float.parseFloat(priceField.getText()),
						(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
						locations,
						transport
					);
				} else {
					throw new IllegalArgumentException("No trip type selected.");
				}

			} catch (IOException ex) {
				System.err.println("Failed to read JSON files: " + ex.getMessage());
				ex.printStackTrace();
			} catch (IllegalArgumentException ex) {
				System.err.println("Invalid selection: " + ex.getMessage());
				ex.printStackTrace();
			}
		});

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
				updateLocations(root2, LocationListView, "Airport");
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
				LocationListView.getSelectionModel().clearSelection();
        		LocationListView.setCellFactory(null);
				updateCompanies(root1, CompanyComboBox, "CruiseCompany");
				updateLocations(root2, LocationListView, "Port");
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
				LocationListView.getSelectionModel().clearSelection();
        		LocationListView.setCellFactory(null);
				updateCompanies(root1, CompanyComboBox, "TrainCompany");
				updateLocations(root2, LocationListView, "TrainStation");
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

	private void updateLocations(JsonNode root, ListView<String> listView, String type) {
		listView.getItems().clear();

		for (JsonNode node : root) {
			if (node.has("type") && node.get("type").asText().equals(type)) {
				listView.getItems().add(node.get("name").asText());
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



	private void displayCompaniesMenu(Scene scene) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayCompaniesMenu
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> {
			displayMenuAdmin(scene);
		});

		VBox back = new VBox(backButton);
		VBox pageContent = new VBox();
		Button CreateCompanyButton = new Button("Create Company");
		CreateCompanyButton.setMinWidth(50);
		CreateCompanyButton.setPrefHeight(50);
		CreateCompanyButton.setOnAction(e -> {
			displayCompanyCreationForm(scene);
		});
		ArrayList<Company> companies = new ArrayList<>();

		// Button EditCompanyButton = new Button("Edit Company");
		// EditCompanyButton.setMinWidth(200);
		// EditCompanyButton.setPrefHeight(50);
		// Button DeleteCompanyButton = new Button("Delete Company");
		// DeleteCompanyButton.setMinWidth(100);
		// DeleteCompanyButton.setPrefHeight(50);
		pageContent.getChildren().addAll(CreateCompanyButton);
		pageContent.setAlignment(Pos.CENTER);
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);
		HBox.setHgrow(pageContent, Priority.ALWAYS);
		HBox layout = new HBox(back, pageContent);

		scene.setRoot(layout);
	}

	private void displayCompanyCreationForm(Scene scene) {
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

		CheckBox flightCheckBox = new CheckBox("Flight Company");
		CheckBox boatCheckBox = new CheckBox("Cruise Company");
		CheckBox trainCheckBox = new CheckBox("Train Company");
		flightCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				boatCheckBox.setSelected(false);
				trainCheckBox.setSelected(false);
			}
		});

		boatCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				flightCheckBox.setSelected(false);
				trainCheckBox.setSelected(false);
			}
		});

		trainCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				flightCheckBox.setSelected(false);
				boatCheckBox.setSelected(false);
			}
		});
		HBox checkboxes = new HBox(5, flightCheckBox, boatCheckBox, trainCheckBox);

		TextField nameField = new TextField();
		nameField.setPromptText("Company Name");

	

		Button submitButton = new Button("Submit");
		submitButton.setMinWidth(100);
		submitButton.setPrefHeight(20);
		submitButton.setOnAction(e -> {
			String type = flightCheckBox.isSelected() ? "FlightCompany" :
				boatCheckBox.isSelected()   ? "CruiseCompany" :
				trainCheckBox.isSelected()  ? "TrainCompany"  : null;
			if (type == null) {
				System.err.println("No company type selected.");
				return;
			}

			CompanyControllerForAdminMenu.goCallCreateCompany(nameField.getText(), type);
		});

		form.getChildren().addAll(
				checkboxes,
				nameField,
				submitButton
		);

		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);

		HBox.setHgrow(form, Priority.ALWAYS);
		layout.getChildren().addAll(back, form);
		scene.setRoot(layout);

	}

	public void setCompanyControllerForAdminMenu(companyController companyControllerForAdminMenu) {
		this.CompanyControllerForAdminMenu = companyControllerForAdminMenu;
	}

}