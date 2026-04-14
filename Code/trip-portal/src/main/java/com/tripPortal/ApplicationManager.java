package com.tripPortal;

import com.tripPortal.Menu.*;
import com.tripPortal.Observateur.AdminDisplay;
import com.tripPortal.Observateur.AdminStation;
import com.tripPortal.Observateur.ClientDisplay;
import com.tripPortal.Observateur.ClientStation;

import javafx.stage.Stage;

import com.tripPortal.Mediateur.*;

public class ApplicationManager {

    private ClientMenu clientMenu;
    private AdminMenu adminMenu;

    private tripController tripController;
    private reservationController reservationController;
    private companyController companyController;
    private locationController locationController;
    private transportController transportController;
    private AdminStation adminStation;
    private ClientStation clientStation;
    private AdminDisplay adminDisplay;
    private ClientDisplay clientDisplay;

    public ApplicationManager() {
        // Shared controllers (optional but smart)
        tripController = new tripController();

        reservationController = new reservationController();
        companyController = new companyController();
        locationController = new locationController();
        transportController = new transportController();

        // Initialize menus
        clientMenu = new ClientMenu();
        adminMenu = new AdminMenu();

        adminStation = new AdminStation(clientMenu);
        clientStation = new ClientStation(adminMenu);
        adminDisplay = new AdminDisplay(adminStation);
        clientDisplay = new ClientDisplay(clientStation);

        // Set up observer relationships
        adminStation.addObserver(adminDisplay);
        clientStation.addObserver(clientDisplay);


        // Inject dependencies
        clientMenu.setTripControllerForClientMenu(tripController);
        clientMenu.setReservationControllerForClientMenu(reservationController);
        clientMenu.setClientStation(clientStation);

        adminMenu.setTripControllerForAdminMenu(tripController);
        adminMenu.setCompanyControllerForAdminMenu(companyController);
        adminMenu.setLocationControllerForAdminMenu(locationController);
        adminMenu.setTransportControllerForAdminMenu(transportController);
        adminMenu.setAdminStation(adminStation);
    }

    public void launchClient(Stage stage) {
        clientMenu.start(stage);
    }

    public void launchAdmin(Stage stage) {
        adminMenu.start(stage);
    }
}
