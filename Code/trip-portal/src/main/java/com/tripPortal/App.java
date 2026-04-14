package com.tripPortal;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class App extends Application {

    // ═══════════════════════════════════════════════════════════════
    // Portail Principal
    // ═══════════════════════════════════════════════════════════════
    private static final String C_BG_DARK  = "#0f1117";
    private static final String C_CARD     = "#1e2535";
    private static final String C_BORDER   = "#2a3450";
    private static final String C_AMBER    = "#f0a500";
    private static final String C_TEXT     = "#e8eaf0";
    private static final String C_MUTED    = "#7a8499";

    private ApplicationManager appManager = new ApplicationManager();

    @Override
    public void start(Stage stage) {

        // ── Brand header ──────────────────────────────────────────
        Label logo = new Label("✈");
        logo.setStyle("-fx-font-size: 48px;");

        Label title = new Label("TripPortal");
        title.setStyle("-fx-text-fill: " + C_AMBER + "; "
                     + "-fx-font-size: 36px; "
                     + "-fx-font-weight: bold;");

        Label subtitle = new Label("Choose how you'd like to continue");
        subtitle.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 14px;");

        VBox header = new VBox(8, logo, title, subtitle);
        header.setAlignment(Pos.CENTER);

        // ── Role cards ────────────────────────────────────────────
        VBox clientCard  = roleCard("👤", "Client",
                "Browse trips, make reservations\nand manage your bookings.",
                () -> appManager.launchClient(stage));

        VBox adminCard   = roleCard("🛠", "Administrator",
                "Manage trips, companies,\nlocations and transports.",
                () -> appManager.launchAdmin(stage));

        HBox cards = new HBox(24, clientCard, adminCard);
        cards.setAlignment(Pos.CENTER);

        // ── Root ──────────────────────────────────────────────────
        VBox root = new VBox(48, header, cards);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + C_BG_DARK + ";");
        root.setPadding(new Insets(60));

        Scene scene = new Scene(root, 860, 560);
        stage.setTitle("TripPortal");
        stage.setScene(scene);
        stage.show();
    }

    // ═══════════════════════════════════════════════════════════════
    // Cartes de rôle
    // ═══════════════════════════════════════════════════════════════
    private VBox roleCard(String icon, String title, String description, Runnable onClick) {
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 36px;");

        Label ttl = new Label(title);
        ttl.setStyle("-fx-text-fill: " + C_TEXT + "; "
                   + "-fx-font-size: 20px; "
                   + "-fx-font-weight: bold;");

        Label desc = new Label(description);
        desc.setStyle("-fx-text-fill: " + C_MUTED + "; -fx-font-size: 12px;");
        desc.setWrapText(true);
        desc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button enterBtn = new Button("Enter  →");
        enterBtn.setPrefHeight(40);
        enterBtn.setMaxWidth(Double.MAX_VALUE);
        enterBtn.setStyle(btnStyle(false));
        enterBtn.setOnMouseEntered(e -> enterBtn.setStyle(btnStyle(true)));
        enterBtn.setOnMouseExited(e  -> enterBtn.setStyle(btnStyle(false)));
        enterBtn.setOnAction(e -> onClick.run());

        VBox card = new VBox(12, ico, ttl, desc, spacer, enterBtn);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(32, 28, 28, 28));
        card.setPrefWidth(260);
        card.setPrefHeight(280);
        card.setStyle(cardStyle(false));
        card.setOnMouseEntered(e -> card.setStyle(cardStyle(true)));
        card.setOnMouseExited(e  -> card.setStyle(cardStyle(false)));
        card.setOnMouseClicked(e -> onClick.run());

        return card;
    }

    private String cardStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? "#243040" : C_CARD) + "; "
             + "-fx-background-radius: 12; "
             + "-fx-border-color: " + (hover ? C_AMBER : C_BORDER) + "; "
             + "-fx-border-radius: 12; "
             + "-fx-cursor: hand;";
    }

    private String btnStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? "#d4920a" : C_AMBER) + "; "
             + "-fx-text-fill: " + C_BG_DARK + "; "
             + "-fx-font-size: 13px; -fx-font-weight: bold; "
             + "-fx-background-radius: 7; -fx-cursor: hand;";
    }

    public static void main(String[] args) {
        launch(args);
    }
}