package ObservateurClasse;

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
	void notifyObservers(ArrayList<Observer> oList);

}