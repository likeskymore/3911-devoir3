package com.tripPortal;

import com.tripPortal.Commande.editCompanyCommand;
import com.tripPortal.Model.Company;

import com.tripPortal.Menu.AdminMenu;
import com.tripPortal.Menu.ClientMenu;
import com.tripPortal.Model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // Top label
        Label welcomeLabel = new Label("Welcome to Trip Portal");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(welcomeLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 0, 0));

        // Buttons
        Button button1 = new Button("Client");
        Button button2 = new Button("Administrator");

        button1.setMaxWidth(400);
        button2.setMaxWidth(400);

        button1.setPrefHeight(100);
        button2.setPrefHeight(100);

        button1.setOnAction(e -> {
            ClientMenu clientMenu = new ClientMenu();
            clientMenu.start(new Stage());
        });

        // Admin click
        button2.setOnAction(e -> {
            AdminMenu adminMenu = new AdminMenu();
            adminMenu.start(new Stage());
        });

        VBox centerBox = new VBox(20, button1, button2);
        centerBox.setAlignment(Pos.CENTER);

        // Layout
        BorderPane root = new BorderPane();

        root.setTop(topBox);
        root.setCenter(centerBox); 

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Trip Portal");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Company company = new Company("test");
        editCompanyCommand test = new editCompanyCommand(company);
        test.execute();
        //launch(args);
    }

    
}