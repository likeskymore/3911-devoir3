package com.tripPortal.Observateur;

import java.time.LocalDate;
import java.util.ArrayList;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.tripPortal.Mediateur.tripController;

public class AdminStation implements Subject {

	private ArrayList<Observer> observers = new ArrayList<>();
	private final ObservableList<Trip> adminViewableTrips = FXCollections.observableArrayList();
	private final tripController controller;
	private String event;

    public AdminStation(tripController controller) {
        this.controller = controller;
		adminViewableTrips.addAll(controller.getAllTrips());
    }

	/**
	 * 
	 * @param o
	 */
	public void addObserver(Observer o) {
		observers.add(o);
	}

	/**
	 * 
	 * @param o
	 */
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

    public void notifyObservers( String event, Object data) {
        for (Observer o : observers) o.update(event, data);
    }

	public void addTrip(Company company, LocalDate startDate, LocalDate endDate,
                        float price, int duration, ArrayList<Location> locations,
                        Transport transport, String type) {

        Trip t = controller.goCallCreateTrip(
            company, startDate, endDate, price, duration, locations, transport, type
        );

        if (t != null) {
			this.event = "TRIP_ADDED";
			notifyObservers(this.event, t);
        }

		Platform.runLater(() -> adminViewableTrips.add(t)); 
    }

	public void removeTrip(String tripId) {
		controller.deleteTrip(tripId);
		adminViewableTrips.removeIf(t -> t.getId().equals(tripId));
		this.event = "TRIP_DELETED";
		notifyObservers(this.event, tripId);
	}

	public void updateTripPrice(String tripId, float newPrice) {
		for (int i = 0; i < adminViewableTrips.size(); i++) {
			Trip t = adminViewableTrips.get(i);
			if (t.getId().equals(tripId)) {
				t.setPrice(newPrice);
				adminViewableTrips.set(i, t); 
				break;
			}
		}
		event = "TRIP_UPDATED";
		notifyObservers(event, tripId);
	}
	
	public ObservableList<Trip> getAdminViewableTrips() {
		return adminViewableTrips;
	}

	public String getUpdate(Observer obj) {
		return this.event;
	}

}