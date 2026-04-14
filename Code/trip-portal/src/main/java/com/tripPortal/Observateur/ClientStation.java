package com.tripPortal.Observateur;

import java.util.ArrayList;

import com.tripPortal.Menu.AdminMenu;
import com.tripPortal.Model.Trip;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientStation implements Subject{

	private ArrayList<Observer> observers = new ArrayList<>();
	private final ObservableList<Trip> clientViewableTrips = FXCollections.observableArrayList();
	private final AdminMenu adminMenu;
	private String event;

    public ClientStation(AdminMenu adminMenu) {
		this.adminMenu = adminMenu;
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

    public void notifyObservers(String event) {
        for (Observer o : observers) o.update(event);
    }

	public ObservableList<Trip> getClientViewableTrips() {
		return clientViewableTrips;
	}


	public String getUpdate(Observer obj) {
		return this.event;
	}

	public AdminMenu getAdminMenu() {
		return adminMenu;
	}
}