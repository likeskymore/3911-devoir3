package com.tripPortal.Menu;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Mediateur.companyController;
import com.tripPortal.Mediateur.locationController;
import com.tripPortal.Mediateur.transportController;
import com.tripPortal.Mediateur.tripController;
import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.CruiseLine;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.Route;
import com.tripPortal.Model.SectionBoat;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.SectionTrain;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Observateur.AdminStation;

import javafx.collections.ObservableList;
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
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AdminMenu {

    // ═══════════════════════════════════════════════════════════════
    // DESIGN TOKENS
    // ═══════════════════════════════════════════════════════════════
    private static final String C_BG_DARK   = "#0f1117";
    private static final String C_SIDEBAR   = "#161b27";
    private static final String C_CARD      = "#1e2535";
    private static final String C_BORDER    = "#2a3450";
    private static final String C_AMBER     = "#f0a500";
    private static final String C_AMBER_DIM = "#a06d00";
    private static final String C_TEXT      = "#e8eaf0";
    private static final String C_MUTED     = "#7a8499";
    private static final String C_SUCCESS   = "#2ecc71";
    private static final String C_DANGER    = "#e74c3c";
    private static final String C_MAIN_BG   = "#111520";

    private tripController       tripControllerForAdminMenu;
    private companyController    CompanyControllerForAdminMenu;
    private locationController   LocationControllerForAdminMenu;
    private transportController  TransportControllerForAdminMenu;
    private AdminStation         adminStationForAdminMenu;

    // ═══════════════════════════════════════════════════════════════
    // SHELL BUILDER  — persistent sidebar + swappable content
    // ═══════════════════════════════════════════════════════════════
    /**
     * Builds the full-screen shell: a fixed sidebar on the left and
     * whatever content pane you pass on the right.
     */
    private HBox buildShell(VBox sidebarContent, VBox mainContent) {
        // Sidebar wrapper
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle("-fx-background-color: " + C_SIDEBAR + ";");

        // Brand header inside sidebar
        Label brand = new Label("✈  TripPortal");
        brand.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 17px; -fx-font-weight: bold; -fx-padding: 28 24 10 24;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + C_BORDER + "; -fx-opacity: 1;");
        sep.setPadding(new Insets(0, 0, 8, 0));

        sidebar.getChildren().addAll(brand, sep, sidebarContent);

        // Main area
        mainContent.setStyle("-fx-background-color: " + C_MAIN_BG + ";");
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        HBox shell = new HBox(sidebar, mainContent);
        shell.setStyle("-fx-background-color: " + C_BG_DARK + ";");
        return shell;
    }

    // ═══════════════════════════════════════════════════════════════
    // SIDEBAR NAV BUTTON
    // ═══════════════════════════════════════════════════════════════
    private Button navBtn(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);
        btn.setStyle(navBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(navBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(navBtnStyle(false)));
        return btn;
    }

    private String navBtnStyle(boolean hover) {
        String bg = hover ? C_AMBER     : "transparent";
        String fg = hover ? C_BG_DARK   : C_TEXT;
        return "-fx-background-color: " + bg + "; "
             + "-fx-text-fill: " + fg + "; "
             + "-fx-font-size: 13px; "
             + "-fx-background-radius: 0; "
             + "-fx-cursor: hand; "
             + "-fx-alignment: CENTER_LEFT; "
             + "-fx-padding: 0 0 0 24;";
    }

	private String activeNavStyle() {
		return "-fx-background-color: " + C_AMBER_DIM + "; "
			+ "-fx-text-fill: " + C_AMBER + "; "
			+ "-fx-font-size: 13px; -fx-font-weight: bold; "
			+ "-fx-background-radius: 0; "
			+ "-fx-alignment: CENTER_LEFT; "
			+ "-fx-padding: 0 0 0 24; "
			+ "-fx-max-width: Infinity; -fx-pref-height: 44;";
	}

    // ═══════════════════════════════════════════════════════════════
    // GENERIC ACTION BUTTON  (amber filled)
    // ═══════════════════════════════════════════════════════════════
    private Button actionBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle(actionBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(actionBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(actionBtnStyle(false)));
        return btn;
    }

    private String actionBtnStyle(boolean hover) {
        String bg = hover ? "#d4920a" : C_AMBER;
        return "-fx-background-color: " + bg + "; "
             + "-fx-text-fill: " + C_BG_DARK + "; "
             + "-fx-font-size: 13px; "
             + "-fx-font-weight: bold; "
             + "-fx-background-radius: 6; "
             + "-fx-cursor: hand; "
             + "-fx-padding: 0 20;";
    }

    // ═══════════════════════════════════════════════════════════════
    // DANGER BUTTON  (red, for delete)
    // ═══════════════════════════════════════════════════════════════
    private Button dangerBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(32);
        btn.setStyle(dangerBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(dangerBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(dangerBtnStyle(false)));
        return btn;
    }

    private String dangerBtnStyle(boolean hover) {
        String bg = hover ? "#c0392b" : C_DANGER;
        return "-fx-background-color: " + bg + "; "
             + "-fx-text-fill: white; "
             + "-fx-font-size: 12px; "
             + "-fx-background-radius: 5; "
             + "-fx-cursor: hand; "
             + "-fx-padding: 0 12;";
    }

    // ═══════════════════════════════════════════════════════════════
    // GHOST BUTTON  (outlined, for secondary actions)
    // ═══════════════════════════════════════════════════════════════
    private Button ghostBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(32);
        btn.setStyle(ghostBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(ghostBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(ghostBtnStyle(false)));
        return btn;
    }

    private String ghostBtnStyle(boolean hover) {
        String bg  = hover ? C_BORDER : "transparent";
        String fg  = hover ? C_TEXT   : C_MUTED;
        return "-fx-background-color: " + bg + "; "
             + "-fx-text-fill: " + fg + "; "
             + "-fx-border-color: " + C_BORDER + "; "
             + "-fx-border-radius: 5; "
             + "-fx-background-radius: 5; "
             + "-fx-font-size: 12px; "
             + "-fx-cursor: hand; "
             + "-fx-padding: 0 12;";
    }

    // ═══════════════════════════════════════════════════════════════
    // PAGE TITLE
    // ═══════════════════════════════════════════════════════════════
    private Label pageTitle(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 24px; -fx-font-weight: bold;");
        return lbl;
    }

    // ═══════════════════════════════════════════════════════════════
    // CARD
    // ═══════════════════════════════════════════════════════════════
    private VBox card(double spacing) {
        VBox c = new VBox(spacing);
        c.setPadding(new Insets(16));
        c.setStyle("-fx-background-color: " + C_CARD + "; "
                 + "-fx-background-radius: 8; "
                 + "-fx-border-color: " + C_BORDER + "; "
                 + "-fx-border-radius: 8;");
        return c;
    }

    // ═══════════════════════════════════════════════════════════════
    // FORM FIELD helper
    // ═══════════════════════════════════════════════════════════════
    private VBox formField(String labelText, javafx.scene.Node input) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
        VBox box = new VBox(4, lbl, input);
        return box;
    }

    private void styleInput(javafx.scene.control.Control ctrl) {
        ctrl.setStyle("-fx-background-color: " + C_BG_DARK + "; "
                    + "-fx-text-fill: " + C_TEXT + "; "
                    + "-fx-border-color: " + C_BORDER + "; "
                    + "-fx-border-radius: 5; "
                    + "-fx-background-radius: 5; "
                    + "-fx-prompt-text-fill: " + C_MUTED + ";");
    }

    private <T> void styleComboBox(ComboBox<T> comboBox, java.util.function.Function<T, String> textExtractor) {
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : textExtractor.apply(item));
                // Selected value shown inside the field (dark background): white text.
                setStyle("-fx-text-fill: " + C_TEXT + "; -fx-background-color: transparent;");
            }
        });
        comboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : textExtractor.apply(item));
                // Open dropdown list: black text for readability on the light/default popup.
                setStyle("-fx-text-fill: #111111; -fx-background-color: transparent;");
            }
        });
    }

    private void styleStringComboBox(ComboBox<String> comboBox) {
        styleComboBox(comboBox, s -> s);
    }

    private void styleTransportComboBox(ComboBox<Transport> comboBox) {
        styleComboBox(comboBox, t -> t.getName());
    }

    // ═══════════════════════════════════════════════════════════════
    // STYLED CHECKBOX
    // ═══════════════════════════════════════════════════════════════
    private CheckBox styledCheckBox(String text) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 13px;");
        return cb;
    }

    // ═══════════════════════════════════════════════════════════════
    // SUCCESS BANNER
    // ═══════════════════════════════════════════════════════════════
    private Label successBanner(String message) {
        Label lbl = new Label("✔  " + message);
        lbl.setStyle("-fx-text-fill: " + C_SUCCESS + "; "
                   + "-fx-background-color: #1a3a28; "
                   + "-fx-padding: 10 16; "
                   + "-fx-background-radius: 6; "
                   + "-fx-font-size: 13px;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    // ═══════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ═══════════════════════════════════════════════════════════════
    public void start(Stage stage) {
        Stage newStage = new Stage();
        Scene scene = new Scene(new VBox(), 900, 600);
        displayMenuAdmin(scene);
        newStage.setTitle("TripPortal — Admin");
        newStage.setScene(scene);
        newStage.show();
    }

    // ═══════════════════════════════════════════════════════════════
    // MAIN MENU
    // ═══════════════════════════════════════════════════════════════
    public void displayMenuAdmin(Scene scene) {
        // Sidebar nav
        VBox nav = new VBox(2);
        nav.setPadding(new Insets(12, 0, 0, 0));

        Button btnTrips      = navBtn("🗺  Trips");
        Button btnCompanies  = navBtn("🏢  Companies");
        Button btnLocations  = navBtn("📍  Locations");
        Button btnTransports = navBtn("🚌  Transports");

        // Spacer pushes logout to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button btnLogout = navBtn("⎋  Logout");

        btnTrips.setOnAction(e      -> displayTripsMenu(scene, ""));
        btnCompanies.setOnAction(e  -> displayCompaniesMenu(scene, ""));
        btnLocations.setOnAction(e  -> displayLocationsMenu(scene, ""));
        btnTransports.setOnAction(e -> displayTransportsMenu(scene));
        btnLogout.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(r -> { if (r == ButtonType.YES) scene.getWindow().hide(); });
        });

        nav.getChildren().addAll(btnTrips, btnCompanies, btnLocations, btnTransports, spacer, btnLogout);

        // Main content — welcome dashboard
        VBox main = new VBox(24);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Dashboard");
        Label subtitle = new Label("Welcome back, Administrator");
        subtitle.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 14px;");

        // Quick-action cards as a strict 2x2 equal grid
        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(16);
        cardsGrid.setVgap(16);
        VBox.setVgrow(cardsGrid, Priority.ALWAYS);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        cardsGrid.getColumnConstraints().addAll(col, col);

        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
        cardsGrid.getRowConstraints().addAll(row, row);

        VBox tripsCard = quickCard("🗺", "Trips", "Create & manage trips", () -> displayTripsMenu(scene, ""));
        VBox companiesCard = quickCard("🏢", "Companies", "Manage travel companies", () -> displayCompaniesMenu(scene, ""));
        VBox locationsCard = quickCard("📍", "Locations", "Airports, ports, stations", () -> displayLocationsMenu(scene, ""));
        VBox transportsCard = quickCard("🚌", "Transports", "Planes, boats, trains", () -> displayTransportsMenu(scene));

        cardsGrid.add(tripsCard, 0, 0);
        cardsGrid.add(companiesCard, 1, 0);
        cardsGrid.add(locationsCard, 0, 1);
        cardsGrid.add(transportsCard, 1, 1);

        GridPane.setHgrow(tripsCard, Priority.ALWAYS);
        GridPane.setHgrow(companiesCard, Priority.ALWAYS);
        GridPane.setHgrow(locationsCard, Priority.ALWAYS);
        GridPane.setHgrow(transportsCard, Priority.ALWAYS);
        GridPane.setVgrow(tripsCard, Priority.ALWAYS);
        GridPane.setVgrow(companiesCard, Priority.ALWAYS);
        GridPane.setVgrow(locationsCard, Priority.ALWAYS);
        GridPane.setVgrow(transportsCard, Priority.ALWAYS);
        GridPane.setFillWidth(tripsCard, true);
        GridPane.setFillWidth(companiesCard, true);
        GridPane.setFillWidth(locationsCard, true);
        GridPane.setFillWidth(transportsCard, true);

        main.getChildren().addAll(title, subtitle, cardsGrid);
        scene.setRoot(buildShell(nav, main));
    }

    private VBox quickCard(String icon, String title, String subtitle, Runnable onClick) {
        Label ico  = new Label(icon);
        ico.setStyle("-fx-font-size: 28px;");
        Label ttl  = new Label(title);
        ttl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label sub  = new Label(subtitle);
        sub.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

        VBox card = new VBox(6, ico, ttl, sub);
        card.setPadding(new Insets(20));
        card.setMinSize(0, 0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMaxHeight(Double.MAX_VALUE);
        card.prefHeightProperty().bind(card.widthProperty());
        card.setStyle(quickCardStyle(false));
        card.setOnMouseEntered(e -> card.setStyle(quickCardStyle(true)));
        card.setOnMouseExited(e  -> card.setStyle(quickCardStyle(false)));
        card.setOnMouseClicked(e -> onClick.run());
        card.setStyle(quickCardStyle(false));
        return card;
    }

    private String quickCardStyle(boolean hover) {
        String border = hover ? C_AMBER : C_BORDER;
        String bg     = hover ? "#1e2d1e".replace("1e2d1e", "243040") : C_CARD;
        return "-fx-background-color: " + bg + "; "
             + "-fx-background-radius: 10; "
             + "-fx-border-color: " + border + "; "
             + "-fx-border-radius: 10; "
             + "-fx-cursor: hand;";
    }

    // ═══════════════════════════════════════════════════════════════
    // TRIPS MENU
    // ═══════════════════════════════════════════════════════════════
    void displayTripsMenu(Scene scene, String message) {
		VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack  = navBtn("←  Back");
		Button btnTrips = navBtn("🗺  Trips");
		btnTrips.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayMenuAdmin(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnTrips);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Manage Trips");
        HBox titleRow = new HBox(16, title);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Button createBtn = actionBtn("＋  Create Trip");
		createBtn.setPrefSize(200, 50);
        Button listBtn   = ghostBtn("☰  View All Trips");
		listBtn.setPrefSize(200, 50);
        createBtn.setOnAction(e -> displayTripCreationForm(scene));
        listBtn.setOnAction(e   -> displayTrips(scene));

        HBox btnRow = new HBox(10, createBtn, listBtn);

        if (message != null && !message.isBlank())
            main.getChildren().addAll(title, successBanner(message), btnRow);
        else
            main.getChildren().addAll(title, btnRow);

        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // TRIP CREATION FORM
    // ═══════════════════════════════════════════════════════════════
    public void displayTripCreationForm(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack  = navBtn("←  Back");
		Button btnTrips = navBtn("🗺  Trips");
		btnTrips.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayTripsMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnTrips);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Create New Trip");

        // Type selection
        CheckBox flightCB = styledCheckBox("✈  Flight");
        CheckBox boatCB   = styledCheckBox("🚢  Cruise");
        CheckBox trainCB  = styledCheckBox("🚂  Route");
        HBox typeRow = new HBox(20, flightCB, boatCB, trainCB);

        VBox typeCard = card(10);
        Label typeLabel = new Label("Trip Type");
        typeLabel.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
        typeCard.getChildren().addAll(typeLabel, typeRow);

        // Company
        ComboBox<String> companyCombo = new ComboBox<>();
        styleInput(companyCombo);
        styleStringComboBox(companyCombo);
        companyCombo.setPrefWidth(280);
        companyCombo.setMaxWidth(Double.MAX_VALUE);
		

        // Locations
        ListView<Location> locationList = new ListView<>();
        locationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        locationList.setPrefHeight(120);
        locationList.setStyle("-fx-background-color: " + C_BG_DARK + "; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 5;");
        Label selectedStopsLabel = new Label("Selected stops: none");
        selectedStopsLabel.setWrapText(true);
        selectedStopsLabel.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

        // Transport
        ComboBox<Transport> transportCombo = new ComboBox<>();
        transportCombo.setConverter(new StringConverter<Transport>() {
            public String toString(Transport t)        { return t != null ? t.getName() : ""; }
            public Transport fromString(String s)      { return null; }
        });
        styleInput(transportCombo);
        styleTransportComboBox(transportCombo);
        transportCombo.setPrefWidth(280);
        transportCombo.setMaxWidth(Double.MAX_VALUE);

        // Dates & price
        DatePicker startDate = new DatePicker();
        DatePicker endDate   = new DatePicker();
        styleInput(startDate);
        styleInput(endDate);
        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 299.99");
        styleInput(priceField);

        // Two-column layout for form fields
        VBox col1 = new VBox(14,
            formField("Company",       companyCombo),
            formField("Transport",     transportCombo),
            formField("Price ($)",     priceField)
        );
        VBox col2 = new VBox(14,
            formField("Departure Date", startDate),
            formField("Return Date",    endDate),
            formField("Stops / Path (Ctrl+click for multi)", locationList),
            selectedStopsLabel
        );
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox formRow = new HBox(24, col1, col2);

        VBox formCard = card(16);
        formCard.getChildren().addAll(typeCard, formRow);

        Button submitBtn = actionBtn("  Create Trip  ");
        submitBtn.setPrefHeight(44);

        // ── Load JSON ──
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root1, root2, root3;
        try {
            root1 = mapper.readTree(new File("src/Database/Company.json"));
            root2 = mapper.readTree(new File("src/Database/Location.json"));
            root3 = mapper.readTree(new File("src/Database/Transport.json"));
        } catch (Exception e) {
            root1 = mapper.createArrayNode();
            root2 = mapper.createArrayNode();
            root3 = mapper.createArrayNode();
        }
        final JsonNode fr1 = root1, fr2 = root2, fr3 = root3;
        final boolean[] updating = { false };
        final boolean[] adjustingStops = { false };
        final ArrayList<Location> selectedStopsSnapshot = new ArrayList<>();

        flightCB.selectedProperty().addListener((o, ov, nv) -> {
            if (updating[0]) return;
            if (nv) { updating[0] = true; boatCB.setSelected(false); trainCB.setSelected(false); updating[0] = false;
                updateCompanies(fr1, companyCombo, "FlightCompany");
                updateLocations(fr2, locationList, "Airport");
                companyCombo.setValue(null);
                transportCombo.getItems().clear();
                transportCombo.setValue(null); }
        });
        boatCB.selectedProperty().addListener((o, ov, nv) -> {
            if (updating[0]) return;
            if (nv) { updating[0] = true; flightCB.setSelected(false); trainCB.setSelected(false); updating[0] = false;
                updateCompanies(fr1, companyCombo, "BoatCompany");
                updateLocations(fr2, locationList, "Port");
                companyCombo.setValue(null);
                transportCombo.getItems().clear();
                transportCombo.setValue(null); }
        });
        trainCB.selectedProperty().addListener((o, ov, nv) -> {
            if (updating[0]) return;
            if (nv) { updating[0] = true; flightCB.setSelected(false); boatCB.setSelected(false); updating[0] = false;
                updateCompanies(fr1, companyCombo, "TrainCompany");
                updateLocations(fr2, locationList, "TrainStation");
                companyCombo.setValue(null);
                transportCombo.getItems().clear();
                transportCombo.setValue(null); }
        });

        companyCombo.valueProperty().addListener((o, ov, nv) -> {
            String transportType = getTransportTypeFromTripSelection(flightCB, boatCB, trainCB);
            if (transportType == null || nv == null || nv.isBlank()) {
                transportCombo.getItems().clear();
                transportCombo.setValue(null);
                return;
            }
            updateTransports(fr3, transportCombo, transportType, nv);
        });

        locationList.getSelectionModel().getSelectedItems().addListener((javafx.collections.ListChangeListener<Location>) change -> {
            if (adjustingStops[0]) {
                return;
            }

            List<Location> selectedStops = locationList.getSelectionModel().getSelectedItems();
            if (flightCB.isSelected() && selectedStops.size() > 2) {
                Location newlyAddedStop = null;
                for (Location stop : selectedStops) {
                    if (!selectedStopsSnapshot.contains(stop)) {
                        newlyAddedStop = stop;
                        break;
                    }
                }
                if (newlyAddedStop == null) {
                    newlyAddedStop = selectedStops.get(selectedStops.size() - 1);
                }

                adjustingStops[0] = true;
                locationList.getSelectionModel().clearSelection(locationList.getItems().indexOf(newlyAddedStop));
                adjustingStops[0] = false;
                selectedStops = new ArrayList<>(locationList.getSelectionModel().getSelectedItems());
            }

            if (selectedStops.isEmpty()) {
                selectedStopsLabel.setText("Selected stops: none");
                selectedStopsSnapshot.clear();
                return;
            }

            StringBuilder selectedStopsText = new StringBuilder("Selected stops: ");
            for (int i = 0; i < locationList.getSelectionModel().getSelectedItems().size(); i++) {
                if (i > 0) selectedStopsText.append(", ");
                selectedStopsText.append(locationList.getSelectionModel().getSelectedItems().get(i).getCity());
            }
            selectedStopsLabel.setText(selectedStopsText.toString());
            selectedStopsSnapshot.clear();
            selectedStopsSnapshot.addAll(locationList.getSelectionModel().getSelectedItems());
        });

        submitBtn.setOnAction(e -> {
            try {
                if (flightCB.isSelected() == false && boatCB.isSelected() == false && trainCB.isSelected() == false) {
                    showError("Please select a trip type.");
                    return;
                }
                if (companyCombo.getValue() == null || companyCombo.getValue().isBlank()) {
                    showError("Please select a company.");
                    return;
                }
                if (transportCombo.getValue() == null) {
                    showError("Please select a transport.");
                    return;
                }
                if (startDate.getValue() == null || endDate.getValue() == null) {
                    showError("Please select both departure and return dates.");
                    return;
                }
                if (priceField.getText() == null || priceField.getText().isBlank()) {
                    showError("Please enter a trip price.");
                    return;
                }

                JsonNode compRoot = mapper.readTree(new File("src/Database/Company.json"));
                Company company = Company.fromJson(companyCombo.getValue(), compRoot);
                ArrayList<Location> locations = new ArrayList<>(locationList.getSelectionModel().getSelectedItems());
                String type = flightCB.isSelected() ? "Flight" : boatCB.isSelected() ? "CruiseLine" : trainCB.isSelected() ? "Route" : null;
                if (type == null) { showError("Please select a trip type."); return; }
                if (flightCB.isSelected() && locations.size() != 2) {
                    showError("Flight trips must have exactly 2 stops/path locations.");
                    return;
                }
                float price;
                try {
                    price = Float.parseFloat(priceField.getText().trim());
                } catch (NumberFormatException numberFormatException) {
                    showError("Please enter a valid trip price.");
                    return;
                }
                adminStationForAdminMenu.addTrip(company, startDate.getValue(), endDate.getValue(),
                    price,
                    (int) ChronoUnit.DAYS.between(startDate.getValue(), endDate.getValue()),
                    locations, transportCombo.getValue(), type);
                displayTripsMenu(scene, "Trip created successfully!");
            } catch (IOException ex) { 
				showError("Filling all fields correctly is required to create a trip."); 
				return;
			}
        });

        main.getChildren().addAll(title, formCard, submitBtn);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // DISPLAY TRIPS
    // ═══════════════════════════════════════════════════════════════
    private void displayTrips(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack  = navBtn("←  Back");
		Button btnTrips = navBtn("🗺  Trips");
		btnTrips.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayTripsMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnTrips);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("All Trips");

        FlowPane grid = new FlowPane(14, 14);
        grid.setPadding(new Insets(4));

        try {
            ObservableList<Trip> tripArray = adminStationForAdminMenu.getAdminViewableTrips();

            for (Trip trip : tripArray) {
                String tripId  = trip.getId();
                String company = trip.getServicedBy().getName();
                String type    = trip.getType();
                String cities  = trip.getDisplayCities();
                String dates   = trip.getDepartureTime() + " — " + trip.getArrivalTime();

                // Trip card
                VBox tc = card(8);
                tc.setPrefWidth(240);

                Label typeLbl   = new Label(type.toUpperCase());
                typeLbl.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");
                Label cityLbl   = new Label(cities);
                cityLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 14px; -fx-font-weight: bold;");
                cityLbl.setWrapText(true);
                Label compLbl   = new Label(company);
                compLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
                Label dateLbl   = new Label(dates);
                dateLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

                Separator s = new Separator();
                s.setStyle("-fx-background-color: " + C_BORDER + ";");

                Button deleteBtn = dangerBtn("🗑  Delete");
                Button updateBtn = ghostBtn("✏  Edit");

                deleteBtn.setOnAction(del -> {
                    tripControllerForAdminMenu.deleteTrip(tripId);
                });
                updateBtn.setOnAction(upd -> displayingTripsToUpdate(scene, trip));

                HBox btnRow = new HBox(8, deleteBtn, updateBtn);
                tc.getChildren().addAll(typeLbl, cityLbl, compLbl, dateLbl, s, btnRow);
                grid.getChildren().add(tc);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        main.getChildren().addAll(title, scroll);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // TRIP UPDATE
    // ═══════════════════════════════════════════════════════════════
    private void displayingTripsToUpdate(Scene scene, Trip trip) {
        VBox nav = new VBox(2);
        nav.setPadding(new Insets(12, 0, 0, 0));
        Button btnBack  = navBtn("←  Back");
        Button btnTrips = navBtn("🗺  Trips");
        btnTrips.setStyle(activeNavStyle());
        btnBack.setOnAction(e -> displayTrips(scene));
        nav.getChildren().addAll(btnBack, new Separator(), btnTrips);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Edit Trip");

        VBox formCard = card(16);

        // ✅ Use model
        Label currentPrice = new Label("Current price: " + trip.getPrice());
        currentPrice.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 13px;");

        TextField priceField = new TextField();
        priceField.setPromptText("New price");
        styleInput(priceField);

        Button confirmBtn = actionBtn("  Save Changes  ");
        confirmBtn.setOnAction(e -> {
            try {
                float newPrice = Float.parseFloat(priceField.getText());

                tripControllerForAdminMenu.updateTripPrice(trip.getId(), newPrice);

            } catch (NumberFormatException ex) {
                System.out.println("Invalid price");
                return;
            }

            displayTrips(scene);
        });

        formCard.getChildren().addAll(
            formField("Price ($)", priceField),
            currentPrice,
            confirmBtn
        );

        main.getChildren().addAll(title, formCard);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // COMPANIES MENU
    // ═══════════════════════════════════════════════════════════════
    private void displayCompaniesMenu(Scene scene, String message) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnCompanies = navBtn("🏢  Companies");
		btnCompanies.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayMenuAdmin(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnCompanies);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Manage Companies");

        Button createBtn = actionBtn("＋  Create Company");
		createBtn.setPrefSize(200, 50);
		Button listBtn   = ghostBtn("☰  View All Companies");
		listBtn.setPrefSize(200, 50);
        createBtn.setOnAction(e -> displayCompanyCreationForm(scene));
		listBtn.setOnAction(e -> displayCompanies(scene));

		HBox btnRow = new HBox(10, createBtn, listBtn);

        if (message != null && !message.isBlank())
            main.getChildren().addAll(title, successBanner(message), btnRow);
        else
            main.getChildren().addAll(title, btnRow);

        scene.setRoot(buildShell(nav, main));
    }

    private void displayCompanies(Scene scene) {

         VBox nav = new VBox(2);
		 nav.setPadding(new Insets(12, 0, 0, 0));
		 Button btnBack      = navBtn("←  Back");
		 Button btnCompanies = navBtn("🏢  Companies");
		 btnCompanies.setStyle(activeNavStyle());
		 btnBack.setOnAction(e -> displayCompaniesMenu(scene, ""));
		 nav.getChildren().addAll(btnBack, new Separator(), btnCompanies);
		 
		 VBox main = new VBox(20);
		 main.setPadding(new Insets(40));
		 Label title = pageTitle("All Companies");

		 FlowPane grid = new FlowPane(14, 14);
		 grid.setPadding(new Insets(4));

		 try {
			 ObjectMapper mapper = new ObjectMapper();
			 File file = new File("src/Database/Company.json");
			 ArrayNode arr = (ArrayNode) mapper.readTree(file);

			 for (JsonNode node : arr) {
                Company company = Company.fromJson(node.get("name").asText(), arr);
                String name = node.get("name").asText();
                String type = node.get("type").asText();

				VBox cc = card(8);
				cc.setPrefWidth(240);

                Label typeLbl   = new Label(type.toUpperCase());
                typeLbl.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");
				Label nameLbl   = new Label(name);
				nameLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 14px; -fx-font-weight: bold;");
                Label tripsLbl  = new Label("Trips: " + node.path("Trips").size());
                tripsLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

				Button deleteBtn = dangerBtn("🗑  Delete");
                deleteBtn.setOnAction(del -> deleteCompany(scene, company));

				Button updateBtn = ghostBtn("✏  Edit");
                updateBtn.setOnAction(upd -> displayCompanyUpdate(scene, company));

				HBox btnRow = new HBox(8, deleteBtn, updateBtn);
                cc.getChildren().addAll(typeLbl, nameLbl, tripsLbl, btnRow);
				grid.getChildren().add(cc);
			 }
		 } catch (IOException ex) { ex.printStackTrace(); }

		 ScrollPane scroll = new ScrollPane(grid);
		 scroll.setFitToWidth(true);
		 scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
		 VBox.setVgrow(scroll, Priority.ALWAYS);

		 main.getChildren().addAll(title, scroll);
		 scene.setRoot(buildShell(nav, main));
    }

    private void deleteCompany(Scene scene, Company company) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete company \"" + company.getName() + "\" and all of its trips?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(result -> {
            if (result != ButtonType.YES) {
                return;
            }

            try {
                ObjectMapper mapper = new ObjectMapper();

                File companyFile = new File("src/Database/Company.json");
                ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
                for (int i = 0; i < companies.size(); i++) {
                    if (companies.get(i).path("id").asText().equals(company.getId())) {
                        companies.remove(i);
                        break;
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);

                File tripFile = new File("src/Database/Trip.json");
                ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
                for (int i = trips.size() - 1; i >= 0; i--) {
                    if (trips.get(i).path("company").asText().equals(company.getName())) {
                        trips.remove(i);
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Unable to delete the company.");
                return;
            }

            displayCompanies(scene);
        });
    }

    // ═══════════════════════════════════════════════════════════════
    // COMPANY UPDATE
    // ═══════════════════════════════════════════════════════════════
    private void displayCompanyUpdate(Scene scene, Company company) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnCompanies = navBtn("🏢  Companies");
		btnCompanies.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayCompaniesMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnCompanies);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Edit: " + company.getName());

        // Rename card
        VBox renameCard = card(12);
        Label renameHdr = new Label("RENAME COMPANY");
        renameHdr.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setPromptText("New company name");
        styleInput(nameField);
        Button confirmBtn = actionBtn("  Save Name  ");
        confirmBtn.setOnAction(e -> {
            String newName = nameField.getText();
            if (newName.isBlank()) { showError("Name cannot be empty."); return; }
            String oldName = company.getName();
            try {
                ObjectMapper m = new ObjectMapper();
                File f = new File("src/Database/Company.json");
                ArrayNode arr = (ArrayNode) m.readTree(f);
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).get("id").asText().equals(company.getId())) {
                        ((ObjectNode) arr.get(i)).put("name", newName);
                        company.setName(newName); break;
                    }
                }
                m.writerWithDefaultPrettyPrinter().writeValue(f, arr);
                // Update trips
                File tf = new File("src/Database/Trip.json");
                ArrayNode ta = (ArrayNode) m.readTree(tf);
                for (int i = 0; i < ta.size(); i++) {
                    if (ta.get(i).get("company").asText().equals(oldName))
                        ((ObjectNode) ta.get(i)).put("company", newName);
                }
                m.writerWithDefaultPrettyPrinter().writeValue(tf, ta);
            } catch (IOException ex) { ex.printStackTrace(); }
            displayCompanyUpdate(scene, company);
        });
        renameCard.getChildren().addAll(renameHdr, formField("New Name", nameField), confirmBtn);

        // Trips card
        VBox tripsCard = card(10);
        Label tripsHdr = new Label("COMPANY TRIPS");
        tripsHdr.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");
        tripsCard.getChildren().add(tripsHdr);

        try {
            ObjectMapper m = new ObjectMapper();
            for (JsonNode node : m.readTree(new File("src/Database/Company.json"))) {
                if (!node.get("id").asText().equals(company.getId())) continue;
                for (JsonNode tripNode : node.get("Trips")) {
                    String tripId = tripNode.asText();
                    HBox row = new HBox(10);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(6, 10, 6, 10));
                    row.setStyle("-fx-background-color: " + C_BG_DARK + "; -fx-background-radius: 5;");

                    Label idLbl = new Label(tripId);
                    idLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 13px;");
                    Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

                    Button delBtn = dangerBtn("🗑");
                    delBtn.setOnAction(ev -> {
                        try {
                            ObjectMapper dm = new ObjectMapper();
                            File df = new File("src/Database/Company.json");
                            JsonNode dr = dm.readTree(df);
                            for (JsonNode c : dr) {
                                if (c.get("id").asText().equals(company.getId())) {
                                    ArrayNode ct = (ArrayNode) c.get("Trips");
                                    for (int i = 0; i < ct.size(); i++) {
                                        if (ct.get(i).asText().equals(tripId)) { ct.remove(i); break; }
                                    }
                                    break;
                                }
                            }
                            dm.writerWithDefaultPrettyPrinter().writeValue(df, dr);
                            File tf = new File("src/Database/Trip.json");
                            ArrayNode ta = (ArrayNode) dm.readTree(tf);
                            for (int i = 0; i < ta.size(); i++) {
                                if (ta.get(i).get("id").asText().equals(tripId)) { ta.remove(i); break; }
                            }
                            dm.writerWithDefaultPrettyPrinter().writeValue(tf, ta);
                        } catch (IOException ex) { ex.printStackTrace(); }
                        displayCompanyUpdate(scene, company);
                    });
                    row.getChildren().addAll(idLbl, sp, delBtn);
                    tripsCard.getChildren().add(row);
                }
            }
        } catch (IOException ex) { ex.printStackTrace(); }

        HBox twoCol = new HBox(20, renameCard, tripsCard);
        HBox.setHgrow(renameCard, Priority.ALWAYS);
        HBox.setHgrow(tripsCard, Priority.ALWAYS);

        main.getChildren().addAll(title, twoCol);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // COMPANY CREATION FORM
    // ═══════════════════════════════════════════════════════════════
    private void displayCompanyCreationForm(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnCompanies = navBtn("🏢  Companies");
		btnCompanies.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayCompaniesMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnCompanies);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Create Company");

        VBox formCard = card(16);

        CheckBox flightCB = styledCheckBox("✈  Flight Company");
        CheckBox boatCB   = styledCheckBox("🚢  Cruise Company");
        CheckBox trainCB  = styledCheckBox("🚂  Train Company");
        flightCB.selectedProperty().addListener((o,ov,nv) -> { if(nv){boatCB.setSelected(false);trainCB.setSelected(false);}});
        boatCB.selectedProperty().addListener((o,ov,nv)   -> { if(nv){flightCB.setSelected(false);trainCB.setSelected(false);}});
        trainCB.selectedProperty().addListener((o,ov,nv)  -> { if(nv){flightCB.setSelected(false);boatCB.setSelected(false);}});
        HBox typeRow = new HBox(20, flightCB, boatCB, trainCB);

        TextField nameField = new TextField();
        nameField.setPromptText("Company Name");
        styleInput(nameField);
        nameField.setMaxWidth(300);

        Button submitBtn = actionBtn("  Create Company  ");
        submitBtn.setOnAction(e -> {
            String type = flightCB.isSelected() ? "FlightCompany"
                        : boatCB.isSelected()   ? "CruiseCompany"
                        : trainCB.isSelected()  ? "TrainCompany" : null;
            if (type == null) { showError("Please select a company type."); return; }
            if (nameField.getText().isBlank()) { showError("Company name cannot be empty."); return; }
            CompanyControllerForAdminMenu.goCallCreateCompany(nameField.getText(), type);
            displayCompaniesMenu(scene, "Company created successfully!");
        });

        formCard.getChildren().addAll(
            formField("Company Type", typeRow),
            formField("Company Name", nameField),
            submitBtn
        );

        main.getChildren().addAll(title, formCard);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // LOCATIONS MENU
    // ═══════════════════════════════════════════════════════════════
    private void displayLocationsMenu(Scene scene, String message) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnLocations = navBtn("📍  Locations");
		btnLocations.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayMenuAdmin(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnLocations);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Manage Locations");

        Button createBtn = actionBtn("＋  Create Location");
		createBtn.setPrefSize(200, 50);
        Button listBtn   = ghostBtn("☰  View All Locations");
		listBtn.setPrefSize(200, 50);
        createBtn.setOnAction(e -> displayLocationCreationForm(scene));
        listBtn.setOnAction(e -> displayLocations(scene));

        HBox btnRow = new HBox(10, createBtn, listBtn);

        if (message != null && !message.isBlank())
            main.getChildren().addAll(title, successBanner(message), btnRow);
        else
            main.getChildren().addAll(title, btnRow);

        scene.setRoot(buildShell(nav, main));
    }

    private void displayLocations(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnLocations = navBtn("📍  Locations");
		btnLocations.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayLocationsMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnLocations);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("All Locations");

        FlowPane grid = new FlowPane(14, 14);
        grid.setPadding(new Insets(4));

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("src/Database/Location.json");
            ArrayNode arr = (ArrayNode) mapper.readTree(file);

            for (JsonNode node : arr) {
                String id = node.path("id").asText();
                String city = node.path("city").asText();
                String type = node.path("type").asText();
                String name = node.path("name").asText(city);
                Location location = switch (type) {
                    case "Airport" -> new Airport(node);
                    case "Port" -> new Port(node);
                    case "TrainStation" -> new TrainStation(node);
                    default -> null;
                };
                if (location == null) {
                    continue;
                }

                VBox cc = card(8);
                cc.setPrefWidth(240);

                Label typeLbl = new Label(type.toUpperCase());
                typeLbl.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");

                Label cityLbl = new Label(city);
                cityLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 14px; -fx-font-weight: bold;");

                Label nameLbl = new Label(name);
                nameLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
                nameLbl.setWrapText(true);

                Label idLbl = new Label("ID: " + id);
                idLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

                Button editBtn = ghostBtn("✏  Edit");
                editBtn.setOnAction(e -> displayLocationUpdate(scene, location));

                Button deleteBtn = dangerBtn("🗑  Delete");
                deleteBtn.setOnAction(e -> deleteLocation(scene, location));

                HBox btnRow = new HBox(8, deleteBtn, editBtn);

                cc.getChildren().addAll(typeLbl, cityLbl, nameLbl, idLbl, btnRow);
                grid.getChildren().add(cc);
            }
        } catch (IOException ex) { ex.printStackTrace(); }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        main.getChildren().addAll(title, scroll);
        scene.setRoot(buildShell(nav, main));
    }

    private void displayLocationUpdate(Scene scene, Location location) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnLocations = navBtn("📍  Locations");
		btnLocations.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayLocations(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnLocations);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Edit Location");

        VBox formCard = card(16);
        Label currentName = new Label("Current name: " + location.getName());
        currentName.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 13px;");
        Label currentCity = new Label("Current city: " + location.getCity());
        currentCity.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 13px;");

        TextField nameField = new TextField();
        nameField.setPromptText("New location name");
        styleInput(nameField);

        TextField cityField = new TextField();
        cityField.setPromptText("New city name");
        styleInput(cityField);

        Button saveBtn = actionBtn("  Save Changes  ");
        saveBtn.setOnAction(e -> {
            String newCity = cityField.getText();
            String newName = nameField.getText();
            if (newCity == null || newCity.isBlank()) {
                showError("City cannot be empty.");
                return;
            }
            if (newName == null || newName.isBlank()) {
                showError("Location name cannot be empty.");
                return;
            }
            if (LocationControllerForAdminMenu == null || !LocationControllerForAdminMenu.editLocation(location, newCity.trim(), newName.trim())) {
                showError("Unable to update the location.");
                return;
            }
            displayLocations(scene);
        });

        formCard.getChildren().addAll(
            currentName,
            currentCity,
            formField("New Name", nameField),
            formField("New City", cityField),
            saveBtn
        );

        main.getChildren().addAll(title, formCard);
        scene.setRoot(buildShell(nav, main));
    }

    private void deleteLocation(Scene scene, Location location) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete location \"" + location.getCity() + "\" and all trips that use it?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(result -> {
            if (result != ButtonType.YES) {
                return;
            }

            if (LocationControllerForAdminMenu == null || !LocationControllerForAdminMenu.deleteLocation(location)) {
                showError("Unable to delete the location.");
                return;
            }

            displayLocations(scene);
        });
    }

    private void displayLocationCreationForm(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack      = navBtn("←  Back");
		Button btnLocations = navBtn("📍  Locations");
		btnLocations.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayLocationsMenu(scene, ""));
		nav.getChildren().addAll(btnBack, new Separator(), btnLocations);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Create Location");

        VBox formCard = card(16);

        CheckBox airportCB = styledCheckBox("✈  Airport");
        CheckBox portCB    = styledCheckBox("⚓  Port");
        CheckBox stationCB = styledCheckBox("🚉  Train Station");
        airportCB.selectedProperty().addListener((o,ov,nv) -> { if(nv){portCB.setSelected(false);stationCB.setSelected(false);}});
        portCB.selectedProperty().addListener((o,ov,nv)    -> { if(nv){airportCB.setSelected(false);stationCB.setSelected(false);}});
        stationCB.selectedProperty().addListener((o,ov,nv) -> { if(nv){airportCB.setSelected(false);portCB.setSelected(false);}});
        HBox typeRow = new HBox(20, airportCB, portCB, stationCB);

        TextField cityField = new TextField();
        cityField.setPromptText("City name");
        styleInput(cityField);
        cityField.setMaxWidth(300);

        TextField locationNameField = new TextField();
        locationNameField.setPromptText("Location name");
        styleInput(locationNameField);
        locationNameField.setMaxWidth(300);

        Button submitBtn = actionBtn("  Create Location  ");
        submitBtn.setOnAction(e -> {
            String type = airportCB.isSelected() ? "Airport" : portCB.isSelected() ? "Port" : stationCB.isSelected() ? "Station" : null;
            if (type == null) { showError("Please select a location type."); return; }
            if (cityField.getText().isBlank()) { showError("City name cannot be empty."); return; }
            if (locationNameField.getText().isBlank()) { showError("Location name cannot be empty."); return; }
            LocationControllerForAdminMenu.goCallCreateLocation(cityField.getText().trim(), locationNameField.getText().trim(), type);
            displayLocationsMenu(scene, "Location created successfully!");
        });

        formCard.getChildren().addAll(
            formField("Location Type", typeRow),
            formField("City Name", cityField),
            formField("Location Name", locationNameField),
            submitBtn
        );

        main.getChildren().addAll(title, formCard);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // TRANSPORTS MENU
    // ═══════════════════════════════════════════════════════════════
    private void displayTransportsMenu(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack       = navBtn("←  Back");
		Button btnTransports = navBtn("🚌  Transports");
		btnTransports.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayMenuAdmin(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnTransports);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Manage Transports");

        Button createBtn = actionBtn("＋  Create Transport");
		createBtn.setPrefSize(200, 50);
        Button listBtn   = ghostBtn("☰  View All Transports");
		listBtn.setPrefSize(200, 50);
        createBtn.setOnAction(e -> displayTransportCreationForm(scene));
        listBtn.setOnAction(e -> displayTransports(scene));

        HBox btnRow = new HBox(10, createBtn, listBtn);

        main.getChildren().addAll(title, btnRow);
        scene.setRoot(buildShell(nav, main));
    }

    private void displayTransports(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack       = navBtn("←  Back");
		Button btnTransports = navBtn("🚌  Transports");
		btnTransports.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayTransportsMenu(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnTransports);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("All Transports");

        FlowPane grid = new FlowPane(14, 14);
        grid.setPadding(new Insets(4));

        try {
            ObjectMapper mapper = new ObjectMapper();
            File transportFile = new File("src/Database/Transport.json");
            File tripFile = new File("src/Database/Trip.json");

            ArrayNode transports = (ArrayNode) mapper.readTree(transportFile);
            ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);

            for (JsonNode transport : transports) {
                String transportId = transport.path("transportID").asText();
                String transportName = transport.path("name").asText("Unnamed");
                String transportType = transport.path("type").asText("Unknown");
                String transportCompany = transport.path("company").asText("");

                int linkedTripsCount = 0;
                for (JsonNode trip : trips) {
                    if (!transportId.equals(trip.path("transport").asText())) {
                        continue;
                    }
                    linkedTripsCount++;
                    if (transportCompany.isBlank()) {
                        transportCompany = trip.path("company").asText("");
                    }
                }

                String companyText = transportCompany.isBlank() ? "Unlinked" : transportCompany;

                VBox tc = card(8);
                tc.setPrefWidth(260);

                Label typeLbl = new Label(transportType.toUpperCase());
                typeLbl.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");

                Label nameLbl = new Label(transportName);
                nameLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 14px; -fx-font-weight: bold;");
                nameLbl.setWrapText(true);

                Label companyLbl = new Label("Company: " + companyText);
                companyLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
                companyLbl.setWrapText(true);

                Label tripsLbl = new Label("Trips linked: " + linkedTripsCount);
                tripsLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");

                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: " + C_BORDER + ";");

                Button deleteBtn = dangerBtn("🗑  Delete");
                deleteBtn.setOnAction(e -> deleteTransport(scene, transportId));

                tc.getChildren().addAll(typeLbl, nameLbl, companyLbl, tripsLbl, sep, deleteBtn);
                grid.getChildren().add(tc);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        main.getChildren().addAll(title, scroll);
        scene.setRoot(buildShell(nav, main));
    }

    private void deleteTransport(Scene scene, String transportId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this transport and all trips linked to it?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(result -> {
            if (result != ButtonType.YES) {
                return;
            }

            try {
                ObjectMapper mapper = new ObjectMapper();

                File transportFile = new File("src/Database/Transport.json");
                ArrayNode transports = (ArrayNode) mapper.readTree(transportFile);
                for (int i = 0; i < transports.size(); i++) {
                    if (transportId.equals(transports.get(i).path("transportID").asText())) {
                        transports.remove(i);
                        break;
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(transportFile, transports);

                File tripFile = new File("src/Database/Trip.json");
                ArrayNode trips = (ArrayNode) mapper.readTree(tripFile);
                ArrayNode removedTripIds = mapper.createArrayNode();
                for (int i = trips.size() - 1; i >= 0; i--) {
                    if (transportId.equals(trips.get(i).path("transport").asText())) {
                        removedTripIds.add(trips.get(i).path("id").asText());
                        trips.remove(i);
                    }
                }
                mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);

                if (removedTripIds.size() > 0) {
                    File companyFile = new File("src/Database/Company.json");
                    ArrayNode companies = (ArrayNode) mapper.readTree(companyFile);
                    for (JsonNode company : companies) {
                        if (!company.has("Trips")) {
                            continue;
                        }
                        ArrayNode companyTrips = (ArrayNode) company.get("Trips");
                        for (int i = companyTrips.size() - 1; i >= 0; i--) {
                            String tripId = companyTrips.get(i).asText();
                            for (JsonNode removedId : removedTripIds) {
                                if (removedId.asText().equals(tripId)) {
                                    companyTrips.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Unable to delete transport.");
                return;
            }

            displayTransports(scene);
        });
    }

    // ═══════════════════════════════════════════════════════════════
    // TRANSPORT CREATION FORM
    // ═══════════════════════════════════════════════════════════════
    private void displayTransportCreationForm(Scene scene) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));
		Button btnBack       = navBtn("←  Back");
		Button btnTransports = navBtn("🚌  Transports");
		btnTransports.setStyle(activeNavStyle());
		btnBack.setOnAction(e -> displayTransportsMenu(scene));
		nav.getChildren().addAll(btnBack, new Separator(), btnTransports);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        Label title = pageTitle("Create Transport");

        TextField nameField = new TextField();
        nameField.setPromptText("Transport name");
        styleInput(nameField);
        nameField.setMaxWidth(300);

        ComboBox<String> companyCombo = new ComboBox<>();
        companyCombo.setPromptText("Select linked company");
        styleInput(companyCombo);
        styleStringComboBox(companyCombo);
        companyCombo.setMaxWidth(300);

        CheckBox planeCB = styledCheckBox("✈  Plane");
        CheckBox boatCB  = styledCheckBox("🚢  Boat");
        CheckBox trainCB = styledCheckBox("🚂  Train");
        HBox typeRow = new HBox(20, planeCB, boatCB, trainCB);

        VBox planeArea = buildPlaneArea();
        VBox boatArea  = buildBoatArea();
        VBox trainArea = buildTrainArea();
        planeArea.setVisible(false); planeArea.setManaged(false);
        boatArea.setVisible(false);  boatArea.setManaged(false);
        trainArea.setVisible(false); trainArea.setManaged(false);

        planeCB.selectedProperty().addListener((o,ov,nv) -> { boatCB.setSelected(false); trainCB.setSelected(false);
            planeArea.setVisible(nv); planeArea.setManaged(nv);
            boatArea.setVisible(false); boatArea.setManaged(false);
            trainArea.setVisible(false); trainArea.setManaged(false);
            updateCompaniesForTransportType(companyCombo, "Plane"); });
        boatCB.selectedProperty().addListener((o,ov,nv) -> { planeCB.setSelected(false); trainCB.setSelected(false);
            boatArea.setVisible(nv); boatArea.setManaged(nv);
            planeArea.setVisible(false); planeArea.setManaged(false);
            trainArea.setVisible(false); trainArea.setManaged(false);
            updateCompaniesForTransportType(companyCombo, "Boat"); });
        trainCB.selectedProperty().addListener((o,ov,nv) -> { planeCB.setSelected(false); boatCB.setSelected(false);
            trainArea.setVisible(nv); trainArea.setManaged(nv);
            planeArea.setVisible(false); planeArea.setManaged(false);
            boatArea.setVisible(false); boatArea.setManaged(false);
            updateCompaniesForTransportType(companyCombo, "Train"); });

        Button submitBtn = actionBtn("  Create Transport  ");
        submitBtn.setOnAction(e -> handleSubmit(scene, nameField, companyCombo, planeCB, boatCB, trainCB, planeArea, boatArea, trainArea));

        VBox formCard = card(16);
        formCard.getChildren().addAll(
			formField("Type", typeRow),
            formField("Company", companyCombo),
            formField("Transport Name", nameField),
            planeArea, boatArea, trainArea,
            submitBtn
        );

        main.getChildren().addAll(title, formCard);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // PLANE / BOAT / TRAIN SECTION BUILDERS  (unchanged logic, restyled)
    // ═══════════════════════════════════════════════════════════════
    private final List<SectionPlane> pendingPlaneSections = new ArrayList<>();
    private VBox pendingPlaneSectionsBox;

    private VBox buildPlaneArea() {
        pendingPlaneSections.clear();
        pendingPlaneSectionsBox = new VBox(6);

        ComboBox<SectionPlane.SectionPlaneType> secTypeCombo = new ComboBox<>();
        secTypeCombo.getItems().addAll(SectionPlane.SectionPlaneType.values());
        secTypeCombo.setPromptText("Section type");
        styleInput(secTypeCombo);
        styleComboBox(secTypeCombo, t -> t.name());

        ComboBox<SectionPlane.Layout> layoutCombo = new ComboBox<>();
        layoutCombo.getItems().addAll(SectionPlane.Layout.values());
        layoutCombo.setPromptText("Layout");
        styleInput(layoutCombo);
        styleComboBox(layoutCombo, l -> l.name());

        Spinner<Integer> rowsSpinner = new Spinner<>(1, 100, 10);
        rowsSpinner.setEditable(true);
        rowsSpinner.setPrefWidth(80);

        Button addBtn = ghostBtn("＋  Add Section");
        addBtn.setOnAction(e -> {
            if (secTypeCombo.getValue() == null || layoutCombo.getValue() == null) { showError("Select type and layout."); return; }
            SectionPlane.SectionPlaneType type = secTypeCombo.getValue();
            if (pendingPlaneSections.stream().anyMatch(s -> s.getSectionType() == type)) { showError("Section already added."); return; }
            SectionPlane sec = new SectionPlane(type, layoutCombo.getValue(), rowsSpinner.getValue());
            pendingPlaneSections.add(sec);
            addSectionRow(pendingPlaneSectionsBox, sec.toString(), () -> pendingPlaneSections.remove(sec));
            secTypeCombo.setValue(null); layoutCombo.setValue(null); rowsSpinner.getValueFactory().setValue(10);
        });

        ScrollPane scroll = new ScrollPane(pendingPlaneSectionsBox);
        scroll.setFitToWidth(true); scroll.setPrefHeight(150);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + C_BG_DARK + ";");

        VBox area = new VBox(10,
            new Label("✈  Plane Sections") {{ setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-weight: bold;"); }},
            new HBox(10, formField("Section Type", secTypeCombo), formField("Layout", layoutCombo), formField("Rows", rowsSpinner)),
            addBtn, scroll);
        area.setPadding(new Insets(14));
        area.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 8; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 8;");
        return area;
    }

    private final List<SectionBoat> pendingBoatSections = new ArrayList<>();
    private VBox pendingBoatSectionsBox;

    private VBox buildBoatArea() {
        pendingBoatSections.clear();
        pendingBoatSectionsBox = new VBox(6);

        ComboBox<SectionBoat.SectionBoatType> secTypeCombo = new ComboBox<>();
        secTypeCombo.getItems().addAll(SectionBoat.SectionBoatType.values());
        secTypeCombo.setPromptText("Section type");
        styleInput(secTypeCombo);
        styleComboBox(secTypeCombo, t -> t.name());

        Label capLabel = new Label("Max capacity: —");
        capLabel.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
        secTypeCombo.valueProperty().addListener((o,ov,val) -> {
            if (val != null) capLabel.setText("Max capacity: " + val.getMaxCapacity() + " per cabin");
        });

        Spinner<Integer> cabinsSpinner = new Spinner<>(1, 500, 10);
        cabinsSpinner.setEditable(true); cabinsSpinner.setPrefWidth(80);

        Button addBtn = ghostBtn("＋  Add Section");
        addBtn.setOnAction(e -> {
            if (secTypeCombo.getValue() == null) { showError("Select a section type."); return; }
            SectionBoat.SectionBoatType type = secTypeCombo.getValue();
            if (pendingBoatSections.stream().anyMatch(s -> s.getSectionType() == type)) { showError("Section already added."); return; }
            SectionBoat sec = new SectionBoat(type, cabinsSpinner.getValue());
            pendingBoatSections.add(sec);
            addSectionRow(pendingBoatSectionsBox, sec.toString(), () -> pendingBoatSections.remove(sec));
            secTypeCombo.setValue(null); cabinsSpinner.getValueFactory().setValue(10);
        });

        ScrollPane scroll = new ScrollPane(pendingBoatSectionsBox);
        scroll.setFitToWidth(true); scroll.setPrefHeight(150);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: " + C_BG_DARK + ";");

        VBox area = new VBox(10,
            new Label("🚢  Boat Sections") {{ setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-weight: bold;"); }},
            new HBox(10, formField("Section Type", secTypeCombo), formField("Cabins", cabinsSpinner)),
            capLabel, addBtn, scroll);
        area.setPadding(new Insets(14));
        area.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 8; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 8;");
        return area;
    }

    @SuppressWarnings("unchecked")
    private VBox buildTrainArea() {
        Spinner<Integer> rowsP = new Spinner<>(1, 100, 10);
        rowsP.setEditable(true); rowsP.setPrefWidth(80);
        Spinner<Integer> rowsE = new Spinner<>(1, 100, 30);
        rowsE.setEditable(true); rowsE.setPrefWidth(80);

        VBox area = new VBox(10,
            new Label("🚂  Train Sections") {{ setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-weight: bold;"); }},
            new HBox(20, formField("First Class (P) — Rows", rowsP), formField("Economy (E) — Rows", rowsE)));
        area.setPadding(new Insets(14));
        area.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 8; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 8;");
        area.setUserData(new Spinner[]{ rowsP, rowsE });
        return area;
    }

    // ═══════════════════════════════════════════════════════════════
    // SUBMIT TRANSPORT
    // ═══════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    private void handleSubmit(Scene scene, TextField nameField, ComboBox<String> companyCombo,
            CheckBox flightCB, CheckBox boatCB, CheckBox trainCB,
            VBox planeArea, VBox boatArea, VBox trainArea) {
        if (nameField.getText().isBlank()) { showError("Please enter a transport name."); return; }
        if (companyCombo.getValue() == null || companyCombo.getValue().isBlank()) {
            showError("Please select the linked company.");
            return;
        }
        String type = flightCB.isSelected() ? "Plane" : boatCB.isSelected() ? "Boat" : trainCB.isSelected() ? "Train" : null;
        if (type == null) { showError("Please select a transport type."); return; }
        switch (type) {
            case "Plane" -> TransportControllerForAdminMenu.goCallCreateTransport(nameField.getText(), type, companyCombo.getValue(), pendingPlaneSections);
            case "Boat"  -> TransportControllerForAdminMenu.goCallCreateTransport(nameField.getText(), type, companyCombo.getValue(), pendingBoatSections);
            case "Train" -> {
                Spinner<Integer>[] spinners = (Spinner<Integer>[]) trainArea.getUserData();
                TransportControllerForAdminMenu.goCallCreateTransport(nameField.getText(), type, companyCombo.getValue(),
                    List.of(new SectionTrain(SectionTrain.SectionTrainType.P, spinners[0].getValue()),
                            new SectionTrain(SectionTrain.SectionTrainType.E, spinners[1].getValue())));
            }
        }
        displayTransportsMenu(scene);
    }

    // ═══════════════════════════════════════════════════════════════
    // SECTION ROW (with remove button)
    // ═══════════════════════════════════════════════════════════════
    private void addSectionRow(VBox container, String label, Runnable onRemove) {
        Label lbl = new Label("✔  " + label);
        lbl.setStyle("-fx-text-fill: " + C_SUCCESS + "; -fx-font-size: 12px;");
        Button rm = dangerBtn("✕");
        HBox row = new HBox(10, lbl, rm);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 8, 4, 8));
        row.setStyle("-fx-background-color: " + C_BG_DARK + "; -fx-background-radius: 4;");
        rm.setOnAction(e -> { onRemove.run(); container.getChildren().remove(row); });
        container.getChildren().add(row);
    }

    // ═══════════════════════════════════════════════════════════════
    // JSON HELPERS
    // ═══════════════════════════════════════════════════════════════
    private void updateCompanies(JsonNode root, ComboBox<String> combo, String type) {
        combo.getItems().clear();
        for (JsonNode n : root)
            if (n.has("type") && n.get("type").asText().equals(type) && n.has("name"))
                combo.getItems().add(n.get("name").asText());
    }

    private void updateLocations(JsonNode root, ListView<Location> list, String type) {
        list.getItems().clear();
        for (JsonNode n : root) {
            if (!n.has("type") || !n.get("type").asText().equals(type)) continue;
            Location loc = switch (n.get("type").asText()) {
                case "Airport"      -> new Airport(n);
                case "Port"         -> new Port(n);
                case "TrainStation" -> new TrainStation(n);
                default -> null;
            };
            if (loc != null) list.getItems().add(loc);
        }
        list.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Location loc, boolean empty) {
                super.updateItem(loc, empty);
                setText(empty || loc == null ? "" : loc.getCity());
                if (empty || loc == null) {
                    setStyle("-fx-background-color: transparent;");
                } else if (isSelected()) {
                    setStyle("-fx-text-fill: " + C_BG_DARK + "; -fx-background-color: " + C_AMBER + ";");
                } else {
                    setStyle("-fx-text-fill: " + C_TEXT + "; -fx-background-color: transparent;");
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (getItem() == null) {
                    return;
                }
                if (selected) {
                    setStyle("-fx-text-fill: " + C_BG_DARK + "; -fx-background-color: " + C_AMBER + ";");
                } else {
                    setStyle("-fx-text-fill: " + C_TEXT + "; -fx-background-color: transparent;");
                }
            }
        });
    }

    private void updateTransports(JsonNode root, ComboBox<Transport> combo, String type, String companyName) {
        combo.getItems().clear();
        combo.setValue(null);
        for (JsonNode n : root) {
            if (!n.has("type") || !n.get("type").asText().equals(type)) continue;
            if (!n.has("company") || !n.get("company").asText().equals(companyName)) continue;
            Transport t = switch (n.get("type").asText()) {
                case "Plane" -> new Plane(n);
                case "Boat"  -> new Boat(n);
                case "Train" -> new Train(n);
                default      -> null;
            };
            if (t != null) combo.getItems().add(t);
        }
    }

    private void updateCompaniesForTransportType(ComboBox<String> companyCombo, String transportType) {
        String companyType = getCompanyTypeForTransportType(transportType);
        companyCombo.getItems().clear();
        companyCombo.setValue(null);
        if (companyType == null) {
            return;
        }

        try {
            JsonNode companyRoot = new ObjectMapper().readTree(new File("src/Database/Company.json"));
            updateCompanies(companyRoot, companyCombo, companyType);
        } catch (IOException e) {
            showError("Unable to load companies.");
        }
    }

    private String getTransportTypeFromTripSelection(CheckBox flightCB, CheckBox boatCB, CheckBox trainCB) {
        if (flightCB.isSelected()) return "Plane";
        if (boatCB.isSelected()) return "Boat";
        if (trainCB.isSelected()) return "Train";
        return null;
    }

    private String getCompanyTypeForTransportType(String transportType) {
        return switch (transportType) {
            case "Plane" -> "FlightCompany";
            case "Boat" -> "BoatCompany";
            case "Train" -> "TrainCompany";
            default -> null;
        };
    }

    // ═══════════════════════════════════════════════════════════════
    // SHOW ERROR
    // ═══════════════════════════════════════════════════════════════
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    // ═══════════════════════════════════════════════════════════════
    // SETTERS
    // ═══════════════════════════════════════════════════════════════
    public void setCompanyControllerForAdminMenu(companyController c)    { this.CompanyControllerForAdminMenu = c; }
    public void setLocationControllerForAdminMenu(locationController c)  { this.LocationControllerForAdminMenu = c; }
    public void setTransportControllerForAdminMenu(transportController c){ this.TransportControllerForAdminMenu = c; }
    public void setTripControllerForAdminMenu(tripController c)          { this.tripControllerForAdminMenu = c; }
    public void setAdminStationAdminMenu(AdminStation c)          { this.adminStationForAdminMenu = c; }
    public tripController getTripController(){ return this.tripControllerForAdminMenu; }
}