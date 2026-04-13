package com.tripPortal.Observateur;


public interface Subject {

	/**
	 * 
	 * @param o
	 */
	void addObserver(Observer o);

	/**
	 * 
	 * @param o
	 */
	void removeObserver(Observer o);

	/**
	 * 
	 * @param oList
	 */
	void notifyObservers(String event, Object data);

	public Object getUpdate(Observer obj);
	

}