package com.tripPortal.Model;

import com.tripPortal.Etat.Available;
import com.tripPortal.Etat.Occupied;
import com.tripPortal.Etat.Reserved;
import com.tripPortal.Etat.SeatState;

public abstract class Seat {

    protected String id;
    protected SeatState currentState;


    public Seat(String id) {
        this.id = id;
        this.currentState = new Available();
    }

    // Retourne l'identifiant lisible du siège — chaque sous-classe définit son
    // format
    public abstract String getSeatID();

    public SeatState getCurrentState() {
        return currentState;
    }

    public void setState(SeatState s) {
        this.currentState = s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOccupied(){ 
        return currentState instanceof Occupied || currentState instanceof Reserved; 
    }

    public String getStateName() {
        return currentState == null ? "Available" : currentState.getClass().getSimpleName();
    }

    public void next(){
        if (currentState != null) {
            currentState.next(this); 
        }
    }

    public void cancel(){
        if (currentState != null) {
            currentState.cancel(this); 
        }
    }


    public abstract float calculatePrice(float basePrice);

    @Override
    public String toString() {
        return getSeatID() + (isOccupied() ? "[X]" : "[O]");
    }
}