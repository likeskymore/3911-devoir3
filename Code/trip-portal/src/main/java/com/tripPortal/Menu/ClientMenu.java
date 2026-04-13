package com.tripPortal.Menu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tripPortal.Mediateur.reservationController;
import com.tripPortal.Mediateur.tripController;
import com.tripPortal.Model.Reservation;
import com.tripPortal.Visiteur.ConcreteCruiseLineVisitor;
import com.tripPortal.Visiteur.ConcreteFlightVisitor;
import com.tripPortal.Visiteur.ConcreteRouteVisitor;
import com.tripPortal.Visiteur.ListTripsDataStructure;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMenu {

    // ═══════════════════════════════════════════════════════════════
    // DESIGN TOKENS  (matches AdminMenu palette)
    // ═══════════════════════════════════════════════════════════════
    private static final String C_BG_DARK  = "#0f1117";
    private static final String C_SIDEBAR  = "#161b27";
    private static final String C_CARD     = "#1e2535";
    private static final String C_BORDER   = "#2a3450";
    private static final String C_AMBER    = "#f0a500";
    private static final String C_TEXT     = "#e8eaf0";
    private static final String C_MUTED    = "#7a8499";
    private static final String C_SUCCESS  = "#2ecc71";
    private static final String C_DANGER   = "#e74c3c";
    private static final String C_MAIN_BG  = "#111520";

    private tripController        tripControllerForClientMenu;
    private reservationController reservationControllerForClientMenu;

    // ═══════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ═══════════════════════════════════════════════════════════════
    public void start(Stage stage) {
        Stage newStage = new Stage();
        Scene scene = new Scene(new VBox(), 900, 600);
        displayMenuClient(scene);
        newStage.setTitle("TripPortal — Client");
        newStage.setScene(scene);
        newStage.show();
    }

    // ═══════════════════════════════════════════════════════════════
    // SHELL  — persistent sidebar + swappable content
    // ═══════════════════════════════════════════════════════════════
    private HBox buildShell(VBox sidebarContent, VBox mainContent) {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);
        sidebar.setStyle("-fx-background-color: " + C_SIDEBAR + ";");

        Label brand = new Label("✈  TripPortal");
        brand.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 17px; -fx-font-weight: bold; -fx-padding: 28 24 10 24;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + C_BORDER + "; -fx-opacity: 1;");
        sep.setPadding(new Insets(0, 0, 8, 0));

        sidebar.getChildren().addAll(brand, sep, sidebarContent);

        mainContent.setStyle("-fx-background-color: " + C_MAIN_BG + ";");
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        HBox shell = new HBox(sidebar, mainContent);
        shell.setStyle("-fx-background-color: " + C_BG_DARK + ";");
        return shell;
    }

    // ═══════════════════════════════════════════════════════════════
    // NAV BUTTON
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
        String bg = hover ? C_AMBER   : "transparent";
        String fg = hover ? C_BG_DARK : C_TEXT;
        return "-fx-background-color: " + bg + "; "
             + "-fx-text-fill: " + fg + "; "
             + "-fx-font-size: 13px; "
             + "-fx-background-radius: 0; "
             + "-fx-cursor: hand; "
             + "-fx-alignment: CENTER_LEFT; "
             + "-fx-padding: 0 0 0 24;";
    }

    private String navBtnActiveStyle() {
        return "-fx-background-color: #a06d00; "
             + "-fx-text-fill: " + C_AMBER + "; "
             + "-fx-font-size: 13px; -fx-font-weight: bold; "
             + "-fx-background-radius: 0; "
             + "-fx-alignment: CENTER_LEFT; "
             + "-fx-padding: 0 0 0 24; "
             + "-fx-max-width: Infinity;  -fx-pref-height: 44;";
    }

    // ═══════════════════════════════════════════════════════════════
    // BUTTON HELPERS
    // ═══════════════════════════════════════════════════════════════
    private Button actionBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(36);
        btn.setStyle(actionBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(actionBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(actionBtnStyle(false)));
        return btn;
    }

    private String actionBtnStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? "#d4920a" : C_AMBER) + "; "
             + "-fx-text-fill: " + C_BG_DARK + "; "
             + "-fx-font-size: 12px; -fx-font-weight: bold; "
             + "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 16;";
    }

    private Button dangerBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(32);
        btn.setStyle(dangerBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(dangerBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(dangerBtnStyle(false)));
        return btn;
    }

    private String dangerBtnStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? "#c0392b" : C_DANGER) + "; "
             + "-fx-text-fill: white; -fx-font-size: 12px; "
             + "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 0 12;";
    }

    private Button ghostBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(32);
        btn.setStyle(ghostBtnStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(ghostBtnStyle(true)));
        btn.setOnMouseExited(e  -> btn.setStyle(ghostBtnStyle(false)));
        return btn;
    }

    private String ghostBtnStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? C_BORDER : "transparent") + "; "
             + "-fx-text-fill: " + (hover ? C_TEXT : C_MUTED) + "; "
             + "-fx-border-color: " + C_BORDER + "; "
             + "-fx-border-radius: 5; -fx-background-radius: 5; "
             + "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 0 12;";
    }

    // ═══════════════════════════════════════════════════════════════
    // CARD / LABEL HELPERS
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

    private Label pageTitle(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 24px; -fx-font-weight: bold;");
        return lbl;
    }

    private Label successBanner(String message) {
        Label lbl = new Label("✔  " + message);
        lbl.setStyle("-fx-text-fill: " + C_SUCCESS + "; "
                   + "-fx-background-color: #1a3a28; "
                   + "-fx-padding: 10 16; -fx-background-radius: 6; -fx-font-size: 13px;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    // ═══════════════════════════════════════════════════════════════
    // MAIN MENU  — dashboard with quick-action cards
    // ═══════════════════════════════════════════════════════════════
    public void displayMenuClient(Scene scene) {
		 VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));

		Button btnProfile = navBtn("👤  My Reservations");
		Button btnReserve = navBtn("🗺  Browse Trips");
		btnProfile.setOnAction(e -> displayProfileMenu(scene));
		btnReserve.setOnAction(e -> displayReserveMenu(scene, ""));

		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		Button btnLogout = navBtn("⎋  Logout");
		btnLogout.setOnAction(e -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
					"Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait().ifPresent(r -> { if (r == ButtonType.YES) scene.getWindow().hide(); });
		});

		nav.getChildren().addAll(btnProfile, btnReserve, spacer, btnLogout);

        // Main content
        VBox main = new VBox(24);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Welcome Back");
        Label subtitle = new Label("Where would you like to go today?");
        subtitle.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 14px;");

        HBox cards = new HBox(20);
        cards.setFillHeight(true);
        VBox.setVgrow(cards, Priority.ALWAYS);

        VBox browseCard = quickCard("🗺", "Browse Trips", "Explore all available trips", () -> displayReserveMenu(scene, ""));
        VBox reservationsCard = quickCard("👤", "My Reservations", "View and manage your bookings", () -> displayProfileMenu(scene));

        HBox.setHgrow(browseCard, Priority.ALWAYS);
        HBox.setHgrow(reservationsCard, Priority.ALWAYS);

        browseCard.prefWidthProperty().bind(cards.widthProperty().subtract(20).divide(2));
        reservationsCard.prefWidthProperty().bind(cards.widthProperty().subtract(20).divide(2));

        cards.getChildren().addAll( reservationsCard, browseCard );

        main.getChildren().addAll(title, subtitle, cards);
        scene.setRoot(buildShell(nav, main));
    }

    private VBox quickCard(String icon, String title, String subtitle, Runnable onClick) {
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 32px;");
        Label ttl = new Label(title);
        ttl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 15px; -fx-font-weight: bold;");
        ttl.setWrapText(true);
        Label sub = new Label(subtitle);
        sub.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 11px;");
        sub.setWrapText(true);

        VBox card = new VBox(8, ico, ttl, sub);
        card.setPadding(new Insets(24));
        card.setMinSize(0, 0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMaxHeight(Double.MAX_VALUE);
        card.prefHeightProperty().bind(card.widthProperty());
        card.setStyle(quickCardStyle(false));
        card.setOnMouseEntered(e -> card.setStyle(quickCardStyle(true)));
        card.setOnMouseExited(e  -> card.setStyle(quickCardStyle(false)));
        card.setOnMouseClicked(e -> onClick.run());
        return card;
    }

    private String quickCardStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? "#243040" : C_CARD) + "; "
             + "-fx-background-radius: 10; "
             + "-fx-border-color: " + (hover ? C_AMBER : C_BORDER) + "; "
             + "-fx-border-radius: 10; -fx-cursor: hand;";
    }

    // ═══════════════════════════════════════════════════════════════
    // PROFILE / RESERVATIONS
    // ═══════════════════════════════════════════════════════════════
	public void displayProfileMenu(Scene scene) {
		VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));

		Button btnBack    = navBtn("←  Back");
		Button btnProfile = navBtn("👤  My Reservations");
		btnBack.setOnAction(e -> displayMenuClient(scene));
		btnProfile.setStyle(navBtnActiveStyle());
		nav.getChildren().addAll(btnBack, new Separator(), btnProfile);

		VBox main = new VBox(20);
		main.setPadding(new Insets(40));

		ArrayList<Reservation> reservations = reservationControllerForClientMenu.fetchUserReservations();
		VBox listBox = new VBox(12);

		if (reservations.isEmpty()) {
			Label empty = new Label("You have no reservations yet.");
			empty.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 14px;");
			Button browseBtn = actionBtn("  Browse Trips  ");
			browseBtn.setOnAction(e -> displayReserveMenu(scene, ""));
			listBox.getChildren().addAll(empty, browseBtn);
		} else {
			// load trip and transport data once for all reservations
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode trips      = mapper.createArrayNode();
			try {
				trips      = (ArrayNode) mapper.readTree(new File("src/Database/Trip.json"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			for (Reservation res : reservations) {
				VBox resCard = card(10);

				// ── header: reservation number + paid badge ──────────
				HBox header = new HBox(10);
				header.setAlignment(Pos.CENTER_LEFT);

				Label numLbl = new Label("Reservation #" + res.getReservationNumber());
				numLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 14px; -fx-font-weight: bold;");

				Region sp = new Region();
				HBox.setHgrow(sp, Priority.ALWAYS);

				String badgeText  = res.isPaid() ? "✔ PAID"    : "⏳ UNPAID";
				String badgeFg    = res.isPaid() ? C_SUCCESS   : C_AMBER;
				String badgeBg    = res.isPaid() ? "#1a3a28"   : "#3a2a00";
				Label paidBadge   = new Label(badgeText);
				paidBadge.setStyle("-fx-text-fill: " + badgeFg + "; -fx-font-size: 11px; -fx-font-weight: bold; "
								+ "-fx-background-color: " + badgeBg + "; "
								+ "-fx-padding: 4 10; -fx-background-radius: 20;");

				header.getChildren().addAll(numLbl, sp, paidBadge);

				// ── details ──────────────────────────────────────────
				Label tripLbl = new Label("Trip ID:   " + res.getTripId());
				tripLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 12px;");

				Label seatLbl = new Label("Seat:       " + res.getReservedSeat());
				seatLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 12px;");

				// ── seat price ───────────────────────────────────────
				String priceText = "Price:      N/A";
				String seatId    = res.getReservedSeat();

				for (JsonNode trip : trips) {
					if (!trip.get("id").asText().equals(res.getTripId())) continue;

					float base     = (float) trip.get("price").asDouble();
					String ttype   = trip.get("type").asText();
					float computed = base;

					if (ttype.equals("Flight")) {
						char section = seatId.charAt(0);
						if (section == 'F')      computed = base * 1.00f;
						else if (section == 'A') computed = base * 0.75f;
						else if (section == 'P') computed = base * 0.60f;
						else if (section == 'E') computed = base * 0.50f;

					} else if (ttype.equals("CruiseLine")) {
						String section = seatId.split("-")[0];
						if (section.equals("D"))      computed = base * 1.00f;
						else if (section.equals("F")) computed = base * 0.90f;
						else if (section.equals("S")) computed = base * 0.90f;
						else if (section.equals("O")) computed = base * 0.75f;
						else if (section.equals("I")) computed = base * 0.50f;

					} else if (ttype.equals("Route")) {
						char section = seatId.charAt(0);
						if (section == 'P')      computed = base * 0.60f;
						else if (section == 'E') computed = base * 0.50f;
					}

					priceText = String.format("Price:      $%.2f", computed);
					break;
				}

				Label priceLbl = new Label(priceText);
				priceLbl.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 12px; -fx-font-weight: bold;");

				// ── separator ────────────────────────────────────────
				Separator sep = new Separator();
				sep.setStyle("-fx-background-color: " + C_BORDER + ";");

				// ── action buttons ───────────────────────────────────
				Button payBtn = actionBtn("💳  Pay Now");
				payBtn.setDisable(res.isPaid());
				payBtn.setOnAction(e -> {
					reservationControllerForClientMenu.payReservation(res);
					displayProfileMenu(scene);
				});

				Button cancelBtn = dangerBtn("🗑  Cancel");
				cancelBtn.setOnAction(e -> {
					reservationControllerForClientMenu.cancelReservation(res);
					displayProfileMenu(scene);
				});

				resCard.getChildren().addAll(header, tripLbl, seatLbl, priceLbl, sep, new HBox(10, payBtn, cancelBtn));
				listBox.getChildren().add(resCard);
			}
		}

		ScrollPane scroll = new ScrollPane(listBox);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
		VBox.setVgrow(scroll, Priority.ALWAYS);

		main.getChildren().addAll(pageTitle("My Reservations"), scroll);
		scene.setRoot(buildShell(nav, main));
	}

    // ═══════════════════════════════════════════════════════════════
    // BROWSE / RESERVE TRIPS
    // ═══════════════════════════════════════════════════════════════
    public void displayReserveMenu(Scene scene, String message) {
		VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));

		Button btnBack    = navBtn("←  Back");
		Button btnReserve = navBtn("🗺  Browse Trips");
		btnBack.setOnAction(e    -> displayMenuClient(scene));
		btnReserve.setStyle(navBtnActiveStyle()); // highlight active

		nav.getChildren().addAll(btnBack, new Separator(), btnReserve);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Available Trips");

        ListTripsDataStructure structure = tripControllerForClientMenu.fetchAllTripsAsStructure().getKey();

        ArrayList<javafx.util.Pair<String, JsonNode>> pairs = new ArrayList<>();
        pairs.addAll(structure.acceptWithNodes(new ConcreteFlightVisitor()));
        pairs.addAll(structure.acceptWithNodes(new ConcreteRouteVisitor()));
        pairs.addAll(structure.acceptWithNodes(new ConcreteCruiseLineVisitor()));

        VBox tripList = new VBox(12);

        if (pairs.isEmpty()) {
            Label empty = new Label("No trips available at the moment.");
            empty.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 14px;");
            tripList.getChildren().add(empty);
        } else {
            for (javafx.util.Pair<String, JsonNode> pair : pairs) {
                String line   = pair.getKey();
                JsonNode node = pair.getValue();

                VBox tripCard = card(8);
                tripCard.setMaxWidth(Double.MAX_VALUE);

                // Parse type for badge
                String type = node.has("type") ? node.get("type").asText() : "Trip";
                String typeIcon = switch (type) {
                    case "Flight"     -> "✈";
                    case "CruiseLine" -> "🚢";
                    case "Route"      -> "🚂";
                    default           -> "🗺";
                };

                Label typeBadge = new Label(typeIcon + "  " + type.toUpperCase());
                typeBadge.setStyle("-fx-text-fill: " + C_AMBER + "; -fx-font-size: 10px; -fx-font-weight: bold;");

                Label infoLbl = new Label(line);
                infoLbl.setWrapText(true);
                infoLbl.setStyle("-fx-text-fill: " + C_TEXT + "; -fx-font-size: 13px;");

                Separator sep = new Separator();
                sep.setStyle("-fx-background-color: " + C_BORDER + ";");

                Button reserveBtn = actionBtn("  Reserve  →");
                reserveBtn.setOnAction(e -> displayAvailableSeats(scene, node));

                HBox footer = new HBox(reserveBtn);
                footer.setAlignment(Pos.CENTER_RIGHT);

                tripCard.getChildren().addAll(typeBadge, infoLbl, sep, footer);
                tripList.getChildren().add(tripCard);
            }
        }

        ScrollPane scroll = new ScrollPane(tripList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        if (message != null && !message.isBlank())
            main.getChildren().addAll(title, successBanner(message), scroll);
        else
            main.getChildren().addAll(title, scroll);

        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // SEAT SELECTION
    // ═══════════════════════════════════════════════════════════════
    public void displayAvailableSeats(Scene scene, JsonNode tripNode) {
        VBox nav = new VBox(2);
		nav.setPadding(new Insets(12, 0, 0, 0));

		Button btnBack    = navBtn("←  Back");
		Button btnReserve = navBtn("🗺  Browse Trips");
		btnBack.setOnAction(e    -> displayReserveMenu(scene, ""));
		btnReserve.setStyle(navBtnActiveStyle());

		nav.getChildren().addAll(btnBack, new Separator(), btnReserve);

        VBox main = new VBox(20);
        main.setPadding(new Insets(40));

        Label title = pageTitle("Select a Seat");

        String type = tripNode.get("type").asText();
        JsonNode transport = tripNode.get("transport");
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/Database/Transport.json");

        VBox content = new VBox(16);

        try {
            JsonNode root = mapper.readTree(file);
            if (root == null || !root.isArray()) return;

            ListView<String> listView = new ListView<>();
            listView.setStyle(
                "-fx-background-color: " + C_BG_DARK + "; "
              + "-fx-border-color: " + C_BORDER + "; "
              + "-fx-border-radius: 6; "
              + "-fx-background-radius: 6;"
            );
            listView.setPrefHeight(300);

            if (type.equals("CruiseLine")) {
                for (JsonNode node : root) {
                    if (node.has("transportID") && node.get("transportID").asText().equals(transport.asText())) {
                        for (JsonNode section : node.get("sections")) {
                            for (JsonNode cabin : section.get("cabins")) {
                                if (!cabin.get("occupied").asBoolean()) {
                                    listView.getItems().add("Cabin ID: " + cabin.get("seatID").asText());
                                }
                            }
                        }
                    }
                }
            } else {
                for (JsonNode node : root) {
                    if (node.has("transportID") && node.get("transportID").asText().equals(transport.asText())) {
                        for (JsonNode section : node.get("sections")) {
                            for (JsonNode seat : section.get("seats")) {
                                if (!seat.get("occupied").asBoolean()) {
                                    listView.getItems().add("Seat ID: " + seat.get("seatID").asText());
                                }
                            }
                        }
                    }
                }
            }

            // Seat count info
            Label countLbl = new Label(listView.getItems().size() + " seats available");
            countLbl.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 12px;");

            VBox seatCard = card(12);
            seatCard.getChildren().addAll(countLbl, listView);

            Button backBtn    = ghostBtn("←  Back to Trips");
            Button reserveBtn = actionBtn("  Confirm Reservation  →");
            reserveBtn.setPrefHeight(42);

            backBtn.setOnAction(e -> displayReserveMenu(scene, ""));
            reserveBtn.setOnAction(e -> {
                String selected = listView.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    new Alert(Alert.AlertType.WARNING, "Please select a seat first.").showAndWait();
                    return;
                }
                String seatID = selected.split(": ")[1];
                reservationControllerForClientMenu.reserveTrip(tripNode, seatID);
                displayReserveMenu(scene, "✔  Reservation confirmed for seat: " + seatID);
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox btnRow = new HBox(10, backBtn, spacer, reserveBtn);
            btnRow.setAlignment(Pos.CENTER_LEFT);

            content.getChildren().addAll(seatCard, btnRow);

        } catch (Exception e) {
            Label errLbl = new Label("Failed to load seat data.");
            errLbl.setStyle("-fx-text-fill: " + C_DANGER + ";");
            content.getChildren().add(errLbl);
        }

        main.getChildren().addAll(title, content);
        scene.setRoot(buildShell(nav, main));
    }

    // ═══════════════════════════════════════════════════════════════
    // SETTERS
    // ═══════════════════════════════════════════════════════════════
    public void setTripControllerForClientMenu(tripController t)               { this.tripControllerForClientMenu = t; }
    public void setReservationControllerForClientMenu(reservationController r) { this.reservationControllerForClientMenu = r; }
}