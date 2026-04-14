package com.tripPortal.Observateur;


public interface Subject {


	void addObserver(Observer o);


	void removeObserver(Observer o);

	void notifyObservers(String event);

	public Object getUpdate(Observer obj);
	

}