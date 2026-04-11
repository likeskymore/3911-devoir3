package com.tripPortal;

import com.tripPortal.Commande.editCompanyCommand;
import com.tripPortal.Model.Company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello JavaFX!");

        Scene scene = new Scene(label, 400, 200);

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