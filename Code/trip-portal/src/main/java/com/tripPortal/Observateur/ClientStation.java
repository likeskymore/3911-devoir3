package com.tripPortal.Observateur;

import java.util.ArrayList;
import java.util.List;

import com.tripPortal.Mediateur.tripController;
import com.tripPortal.Model.Trip;
import com.tripPortal.Visiteur.ListTripsDataStructure;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class ClientStation implements Subject{

	private ArrayList<Observer> observers = new ArrayList<>();
	private final ObservableList<Trip> clientViewableTrips = FXCollections.observableArrayList();
	private final SimpleStringProperty seatCount = new SimpleStringProperty("Seats booked: 0");
	private final tripController controller;
	private String event;

    public ClientStation(tripController controller) {
        this.controller = controller;
		clientViewableTrips.addAll(fetchVisitableTrips().getValue());
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

    public void notifyObservers(String event, Object data) {
        for (Observer o : observers) o.update(event, data);
    }

	public void bookSeat(Trip t) {
        incrementSeatCount();
		this.event = "SEAT_BOOKED";
        notifyObservers(event, t);
    }

	public ObservableList<Trip> getClientViewableTrips() {
		return clientViewableTrips;
	}

	private Pair<ListTripsDataStructure, List<Trip>> fetchVisitableTrips() {
		return controller.fetchAllTripsAsStructure();
	}

	private void incrementSeatCount() {
		int currentCount = Integer.parseInt(seatCount.get().split(": ")[1]);
		seatCount.set("Seats booked: " + (currentCount + 1));
	}

	public void refreshTrips() {
    	clientViewableTrips.setAll(fetchVisitableTrips().getValue());
	}


	public String getUpdate(Observer obj) {
		return this.event;
	}
}