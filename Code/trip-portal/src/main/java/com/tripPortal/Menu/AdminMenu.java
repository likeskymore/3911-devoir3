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
import com.tripPortal.Mediateur.locationController;
import com.tripPortal.Mediateur.transportController;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.SectionBoat;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.SectionTrain;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu {
	private companyController CompanyControllerForAdminMenu;
	private locationController LocationControllerForAdminMenu;
	private transportController TransportControllerForAdminMenu;

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
		Button editProfileButton = new Button("Profile");
		editProfileButton.setMinWidth(200);
		editProfileButton.setPrefHeight(50);

		Button tripsButton = new Button("Trips");
		tripsButton.setMinWidth(200);
		tripsButton.setPrefHeight(50);
		tripsButton.setOnAction(e -> {
			displayTripsMenu(scene);
		});

		Button companiesButton = new Button("Companies");
		companiesButton.setMinWidth(200);
		companiesButton.setPrefHeight(50);
		companiesButton.setOnAction(e -> {
			displayCompaniesMenu(scene);
		});
		Button locationsButton = new Button("Locations");
		locationsButton.setMinWidth(200);
		locationsButton.setPrefHeight(50);
		locationsButton.setOnAction(e -> {
			displayLocationsMenu(scene, "");
		});

		Button transportsButton = new Button("Transports");
		transportsButton.setMinWidth(200);
		transportsButton.setPrefHeight(50);
		transportsButton.setOnAction(e -> {
			displayTransportsMenu(scene);
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

		root.getChildren().addAll(editProfileButton, tripsButton, companiesButton, locationsButton, transportsButton,
				logoutButton);

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
		CreateTripButton.setMinWidth(100);
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
		LocationListView.setPrefHeight(100);
		LocationListView.setMaxWidth(350);
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
							transport);
				} else if (boatCheckBox.isSelected()) {
					trip = CruiseLineFactory.getInstance().createTrajectory(
							company,
							startDate.getValue(),
							endDate.getValue(),
							Float.parseFloat(priceField.getText()),
							(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
							locations,
							transport);
				} else if (trainCheckBox.isSelected()) {
					trip = RouteFactory.getInstance().createTrajectory(
							company,
							startDate.getValue(),
							endDate.getValue(),
							Float.parseFloat(priceField.getText()),
							(int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
							locations,
							transport);
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
		final boolean[] updating = { false };

		// ---------------- LOGIC ----------------
		flightCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (updating[0])
				return;
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
			if (updating[0])
				return;
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
			if (updating[0])
				return;
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
				submitButton);

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
		CreateCompanyButton.setMinWidth(100);
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
			String type = flightCheckBox.isSelected() ? "FlightCompany"
					: boatCheckBox.isSelected() ? "CruiseCompany" : trainCheckBox.isSelected() ? "TrainCompany" : null;

			if (type == null) {
				showError("Please select a company type.");
				return;
			}

			if (nameField.getText().isEmpty()) {
				showError("Company name cannot be empty.");
				return;
			}

			CompanyControllerForAdminMenu.goCallCreateCompany(nameField.getText(), type);
		});

		form.getChildren().addAll(
				checkboxes,
				nameField,
				submitButton);

		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);

		HBox.setHgrow(form, Priority.ALWAYS);
		layout.getChildren().addAll(back, form);
		scene.setRoot(layout);

	}

	private void displayLocationsMenu(Scene scene, String message) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayCompaniesMenu
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> {
			displayMenuAdmin(scene);
		});

		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

		VBox back = new VBox(backButton);
		VBox pageContent = new VBox();
		Button CreateLocationButton = new Button("Create Location");
		CreateLocationButton.setMinWidth(100);
		CreateLocationButton.setPrefHeight(50);
		CreateLocationButton.setOnAction(e -> {
			displayLocationCreationForm(scene);
		});

		// Button EditLocationButton = new Button("Edit Location");
		// EditLocationButton.setMinWidth(200);
		// EditLocationButton.setPrefHeight(50);
		// Button DeleteLocationButton = new Button("Delete Location");
		// DeleteLocationButton.setMinWidth(100);
		// DeleteLocationButton.setPrefHeight(50);
		pageContent.getChildren().addAll(messageLabel, CreateLocationButton);
		pageContent.setAlignment(Pos.CENTER);
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);
		HBox.setHgrow(pageContent, Priority.ALWAYS);
		HBox layout = new HBox(back, pageContent);

		scene.setRoot(layout);
	}

	private void displayLocationCreationForm(Scene scene) {

		HBox layout = new HBox();

		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> displayLocationsMenu(scene, ""));
		VBox back = new VBox(backButton);

		// ---------------- FORM ----------------
		VBox form = new VBox(10);
		form.setAlignment(Pos.CENTER);

		Label typeLabel = new Label("Select Location Type:");
		CheckBox airportCheckBox = new CheckBox("Airport");
		CheckBox portCheckBox = new CheckBox("Port");
		CheckBox stationCheckBox = new CheckBox("Train Station");
		airportCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				portCheckBox.setSelected(false);
				stationCheckBox.setSelected(false);
			}
		});

		portCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				airportCheckBox.setSelected(false);
				stationCheckBox.setSelected(false);
			}
		});

		stationCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				airportCheckBox.setSelected(false);
				portCheckBox.setSelected(false);
			}
		});
		HBox checkboxes = new HBox(5, airportCheckBox, portCheckBox, stationCheckBox);

		TextField nameField = new TextField();
		nameField.setPromptText("Location Name (City)");

		Button submitButton = new Button("Submit");
		submitButton.setMinWidth(100);
		submitButton.setPrefHeight(20);
		submitButton.setOnAction(e -> {
			String type = airportCheckBox.isSelected() ? "Airport"
					: portCheckBox.isSelected() ? "Port" : stationCheckBox.isSelected() ? "Station" : null;
			if (type == null) {
				showError("Please select a location type.");
				return;
			}
			
			if (nameField.getText().isEmpty()) {
				showError("Location name cannot be empty.");
				return;
			}

			LocationControllerForAdminMenu.goCallCreateLocation(nameField.getText(), type);
			displayLocationsMenu(scene, "Location created successfully!");
		});

		form.getChildren().addAll(
				typeLabel,
				checkboxes,
				nameField,
				submitButton);

		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);

		HBox.setHgrow(form, Priority.ALWAYS);
		layout.getChildren().addAll(back, form);
		scene.setRoot(layout);

	}

	private void displayTransportsMenu(Scene scene) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.displayTransportsMenu
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> {
			displayMenuAdmin(scene);
		});

		VBox back = new VBox(backButton);
		VBox pageContent = new VBox();
		Button CreateTransportButton = new Button("Create Transport");
		CreateTransportButton.setMinWidth(100);
		CreateTransportButton.setPrefHeight(50);
		CreateTransportButton.setOnAction(e -> {
			displayTransportCreationForm(scene);
		});

		pageContent.getChildren().addAll(CreateTransportButton);
		pageContent.setAlignment(Pos.CENTER);
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);
		HBox.setHgrow(pageContent, Priority.ALWAYS);
		HBox layout = new HBox(back, pageContent);

		scene.setRoot(layout);
	}

	private void displayTransportCreationForm(Scene scene) {

		// ── Bouton Retour ──────────────────────────────────────────────
		Button backButton = new Button("Back");
		backButton.setMinWidth(100);
		backButton.setPrefHeight(50);
		backButton.setOnAction(e -> displayTransportsMenu(scene));
		VBox back = new VBox(backButton);
		back.setPrefWidth(200);
		back.setMinWidth(200);
		back.setMaxWidth(200);

		// ── Nom du transport ───────────────────────────────────────────
		TextField nameField = new TextField();
		nameField.setPromptText("Transport Name");

		// ── Sélection du type ──────────────────────────────────────────
		CheckBox flightCheckBox = new CheckBox("Plane");
		CheckBox boatCheckBox = new CheckBox("Boat");
		CheckBox trainCheckBox = new CheckBox("Train");
		HBox checkboxes = new HBox(10, flightCheckBox, boatCheckBox, trainCheckBox);

		// ── Zone dynamique selon le type ───────────────────────────────
		VBox planeArea = buildPlaneArea();
		VBox boatArea = buildBoatArea();
		VBox trainArea = buildTrainArea();
		planeArea.setVisible(false);
		planeArea.setManaged(false);
		boatArea.setVisible(false);
		boatArea.setManaged(false);
		trainArea.setVisible(false);
		trainArea.setManaged(false);

		// Comportement radio + affichage de la bonne zone
		flightCheckBox.selectedProperty().addListener((obs, o, newVal) -> {
			boatCheckBox.setSelected(false);
			trainCheckBox.setSelected(false);
			planeArea.setVisible(newVal);
			planeArea.setManaged(newVal);
			boatArea.setVisible(false);
			boatArea.setManaged(false);
			trainArea.setVisible(false);
			trainArea.setManaged(false);
		});
		boatCheckBox.selectedProperty().addListener((obs, o, newVal) -> {
			flightCheckBox.setSelected(false);
			trainCheckBox.setSelected(false);
			boatArea.setVisible(newVal);
			boatArea.setManaged(newVal);
			planeArea.setVisible(false);
			planeArea.setManaged(false);
			trainArea.setVisible(false);
			trainArea.setManaged(false);
		});
		trainCheckBox.selectedProperty().addListener((obs, o, newVal) -> {
			flightCheckBox.setSelected(false);
			boatCheckBox.setSelected(false);
			trainArea.setVisible(newVal);
			trainArea.setManaged(newVal);
			planeArea.setVisible(false);
			planeArea.setManaged(false);
			boatArea.setVisible(false);
			boatArea.setManaged(false);
		});

		// ── Bouton Submit ──────────────────────────────────────────────
		Button submitButton = new Button("Submit");
		submitButton.setMinWidth(100);
		submitButton.setPrefHeight(30);
		submitButton.setOnAction(e -> handleSubmit(
				scene, nameField,
				flightCheckBox, boatCheckBox, trainCheckBox,
				planeArea, boatArea, trainArea));

		// ── Assemblage ─────────────────────────────────────────────────
		VBox form = new VBox(12,
				new Label("Type de transport :"), checkboxes,
				new Label("Nom :"), nameField,
				planeArea, boatArea, trainArea,
				submitButton);
		form.setAlignment(Pos.TOP_LEFT);
		form.setPadding(new Insets(20));

		HBox.setHgrow(form, Priority.ALWAYS);
		HBox layout = new HBox(back, form);
		scene.setRoot(layout);
	}

	// ══════════════════════════════════════════════════════════════════
	// ZONE AVION
	// ══════════════════════════════════════════════════════════════════
	private final List<SectionPlane> pendingPlaneSections = new ArrayList<>();
	private VBox pendingPlaneSectionsBox;

	private VBox buildPlaneArea() {
		pendingPlaneSections.clear();
		pendingPlaneSectionsBox = new VBox(4);

		// Sélecteurs
		ComboBox<SectionPlane.SectionPlaneType> secTypeCombo = new ComboBox<>();
		secTypeCombo.getItems().addAll(SectionPlane.SectionPlaneType.values());
		secTypeCombo.setPromptText("Type (F/A/P/E)");

		ComboBox<SectionPlane.Layout> layoutCombo = new ComboBox<>();
		layoutCombo.getItems().addAll(SectionPlane.Layout.values());
		layoutCombo.setPromptText("Disposition (S/C/M/L)");

		Spinner<Integer> rowsSpinner = new Spinner<>(1, 100, 10);
		rowsSpinner.setEditable(true);
		rowsSpinner.setPrefWidth(80);

		Button addSecBtn = new Button("+ Ajouter section");
		addSecBtn.setOnAction(e -> {
			if (secTypeCombo.getValue() == null || layoutCombo.getValue() == null) {
				showError("Veuillez choisir un type et une disposition.");
				return;
			}
			SectionPlane.SectionPlaneType type = secTypeCombo.getValue();
			boolean duplicate = pendingPlaneSections.stream()
					.anyMatch(s -> s.getSectionType() == type);
			if (duplicate) {
				showError("La section " + type + " est déjà ajoutée.");
				return;
			}
			SectionPlane section = new SectionPlane(type, layoutCombo.getValue(), rowsSpinner.getValue());
			pendingPlaneSections.add(section);
			addSectionRow(pendingPlaneSectionsBox, section.toString(), () -> pendingPlaneSections.remove(section));

			secTypeCombo.setValue(null);
			layoutCombo.setValue(null);
			rowsSpinner.getValueFactory().setValue(10);
		});

		ScrollPane scrollPane = new ScrollPane(pendingPlaneSectionsBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(200); // hauteur fixe — scroll si ça dépasse
		scrollPane.setStyle("-fx-background-color: transparent;");

		VBox area = new VBox(8,
				new Label("─── Sections de l'avion ───"),
				new HBox(8, new Label("Type :"), secTypeCombo),
				new HBox(8, new Label("Disposition :"), layoutCombo),
				new HBox(8, new Label("Rangées :"), rowsSpinner),
				addSecBtn,
				scrollPane);
		area.setStyle("-fx-border-color: #aaa; -fx-border-radius:5; -fx-padding:10;");
		return area;
	}

	// ══════════════════════════════════════════════════════════════════
	// ZONE BATEAU
	// ══════════════════════════════════════════════════════════════════
	private final List<SectionBoat> pendingBoatSections = new ArrayList<>();
	private VBox pendingBoatSectionsBox;

	private VBox buildBoatArea() {
		pendingBoatSections.clear();
		pendingBoatSectionsBox = new VBox(4);

		ComboBox<SectionBoat.SectionBoatType> secTypeCombo = new ComboBox<>();
		secTypeCombo.getItems().addAll(SectionBoat.SectionBoatType.values());
		secTypeCombo.setPromptText("Section (I/O/S/F/D)");

		// Affiche la capacité max selon la section choisie
		Label capacityLabel = new Label("Capacité max : -");
		secTypeCombo.valueProperty().addListener((obs, o, val) -> {
			if (val != null)
				capacityLabel.setText("Capacité max : " + val.getMaxCapacity() + " personnes/cabine");
		});

		Spinner<Integer> cabinsSpinner = new Spinner<>(1, 500, 10);
		cabinsSpinner.setEditable(true);
		cabinsSpinner.setPrefWidth(80);

		Button addSecBtn = new Button("+ Ajouter section");
		addSecBtn.setOnAction(e -> {
			if (secTypeCombo.getValue() == null) {
				showError("Veuillez choisir un type de section.");
				return;
			}
			SectionBoat.SectionBoatType type = secTypeCombo.getValue();
			boolean duplicate = pendingBoatSections.stream()
					.anyMatch(s -> s.getSectionType() == type);
			if (duplicate) {
				showError("La section " + type + " est déjà ajoutée.");
				return;
			}
			SectionBoat section = new SectionBoat(type, cabinsSpinner.getValue());
			pendingBoatSections.add(section);
			addSectionRow(pendingBoatSectionsBox, section.toString(), () -> pendingBoatSections.remove(section));

			secTypeCombo.setValue(null);
			cabinsSpinner.getValueFactory().setValue(10);
			capacityLabel.setText("Capacité max : -");
		});

		ScrollPane scrollPane = new ScrollPane(pendingBoatSectionsBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(200);
		scrollPane.setStyle("-fx-background-color: transparent;");

		VBox area = new VBox(8,
				new Label("─── Sections du paquebot ───"),
				new HBox(8, new Label("Type :"), secTypeCombo),
				capacityLabel,
				new HBox(8, new Label("Cabines :"), cabinsSpinner),
				addSecBtn,
				scrollPane);
		area.setStyle("-fx-border-color: #aaa; -fx-border-radius:5; -fx-padding:10;");
		return area;
	}

	// ══════════════════════════════════════════════════════════════════
	// ZONE TRAIN (P + E automatiques, on configure juste les rangées)
	// ══════════════════════════════════════════════════════════════════
	private VBox buildTrainArea() {
		Spinner<Integer> rowsP = new Spinner<>(1, 100, 10);
		rowsP.setEditable(true);
		rowsP.setPrefWidth(80);

		Spinner<Integer> rowsE = new Spinner<>(1, 100, 30);
		rowsE.setEditable(true);
		rowsE.setPrefWidth(80);

		VBox area = new VBox(8,
				new Label("─── Sections du train (Étroit S — 3 colonnes) ───"),
				new HBox(8, new Label("Première (P) — rangées :"), rowsP),
				new HBox(8, new Label("Économie  (E) — rangées :"), rowsE));
		area.setStyle("-fx-border-color: #aaa; -fx-border-radius:5; -fx-padding:10;");
		area.setUserData(new Spinner[] { rowsP, rowsE }); // récupéré au submit
		return area;
	}

	// ══════════════════════════════════════════════════════════════════
	// SUBMIT
	// ══════════════════════════════════════════════════════════════════
	private void handleSubmit(Scene scene, TextField nameField,
			CheckBox flightCB, CheckBox boatCB, CheckBox trainCB,
			VBox planeArea, VBox boatArea, VBox trainArea) {

		if (nameField.getText().isBlank()) {
			showError("Veuillez entrer un nom de transport.");
			return;
		}

		String type = flightCB.isSelected() ? "Plane"
				: boatCB.isSelected() ? "Boat" : trainCB.isSelected() ? "Train" : null;
		if (type == null) {
			showError("Veuillez sélectionner un type de transport.");
			return;
		}

		switch (type) {
			case "Plane" -> {
				TransportControllerForAdminMenu.goCallCreateTransport(
						nameField.getText(), type, pendingPlaneSections);
			}
			case "Boat" -> {
				TransportControllerForAdminMenu.goCallCreateTransport(
						nameField.getText(), type, pendingBoatSections);
			}
			case "Train" -> {
				Spinner[] spinners = (Spinner[]) trainArea.getUserData();
				int rowsP = (int) spinners[0].getValue();
				int rowsE = (int) spinners[1].getValue();
				List<SectionTrain> trainSections = List.of(
						new SectionTrain(SectionTrain.SectionTrainType.P, rowsP),
						new SectionTrain(SectionTrain.SectionTrainType.E, rowsE));
				TransportControllerForAdminMenu.goCallCreateTransport(
						nameField.getText(), type, trainSections);
			}
		}

		displayTransportsMenu(scene);
	}

	// ══════════════════════════════════════════════════════════════════
	// UTILITAIRES
	// ══════════════════════════════════════════════════════════════════

	/** Ajoute une ligne avec un bouton ✕ pour supprimer */
	private void addSectionRow(VBox container, String label, Runnable onRemove) {
		Label lbl = new Label("✔ " + label);
		Button removeBtn = new Button("✕");
		HBox row = new HBox(8, lbl, removeBtn);
		row.setAlignment(Pos.CENTER_LEFT);
		removeBtn.setOnAction(e -> {
			onRemove.run();
			container.getChildren().remove(row);
		});
		container.getChildren().add(row);
	}

	private void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait();
	}

	public void setCompanyControllerForAdminMenu(companyController companyControllerForAdminMenu) {
		this.CompanyControllerForAdminMenu = companyControllerForAdminMenu;
	}

	public void setLocationControllerForAdminMenu(locationController locationControllerForAdminMenu) {
		this.LocationControllerForAdminMenu = locationControllerForAdminMenu;
	}

	public void setTransportControllerForAdminMenu(transportController transportControllerForAdminMenu) {
		this.TransportControllerForAdminMenu = transportControllerForAdminMenu;
	}

}