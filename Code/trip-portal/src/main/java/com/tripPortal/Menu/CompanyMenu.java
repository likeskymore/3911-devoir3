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
import com.tripPortal.Mediateur.companyController;

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

public class CompanyMenu{

    public void start(Stage stage) {
		// TODO - implement com.tripPortal.Menu.AdminMenu.start
		Stage newStage = new Stage();

		Scene scene = new Scene(new VBox(), 600, 400);
		displayMenuCompany(scene);

		newStage.setTitle("Company");
		newStage.setScene(scene);

		newStage.show();
	}

    public void displayMenuCompany(Scene scene){
        Label label = new Label("Please enter the id of your comapny");
        TextField textField = new TextField();

        HBox hbox = new HBox(10,label, textField);
        hbox.setAlignment(Pos.CENTER);

        VBox root = new VBox(hbox);
        scene.setRoot(root);
    }
}