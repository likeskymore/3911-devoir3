package com.tripPortal.Observateur;

import java.util.ArrayList;

import com.tripPortal.Menu.ClientMenu;

public class AdminStation implements Subject {

	private ArrayList<Observer> observers = new ArrayList<>();
	private final ClientMenu clientMenu;
	private String event;

    public AdminStation(ClientMenu clientMenu) {
        this.clientMenu = clientMenu;
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

	public String getUpdate(Observer obj) {
		return this.event;
	}

	public ClientMenu getClientMenu() {
		return clientMenu;
	}

}