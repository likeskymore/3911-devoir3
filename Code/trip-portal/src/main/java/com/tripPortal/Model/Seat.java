package com.tripPortal.Model;

import com.tripPortal.Etat.Available;
import com.tripPortal.Etat.Occupied;
import com.tripPortal.Etat.Reserved;
import com.tripPortal.Etat.SeatState;

public abstract class Seat {

    protected String id; // identifiant unique du siège (ex: "F12A", "Cabin-3", "2A")
    protected SeatState currentState;


    public Seat(String id) {
        this.id = id;
        this.currentState = new Available();
    }

    // ── Méthode abstraite ─────────────────────────────────────────

    // Retourne l'identifiant lisible du siège — chaque sous-classe définit son
    // format
    public abstract String getSeatID();

    // ── Méthodes communes ─────────────────────────────────────────

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

    public SeatState getCurrentState() {
        return currentState;
    }

    public abstract float calculatePrice(float basePrice);

    @Override
    public String toString() {
        return getSeatID() + (isOccupied() ? "[X]" : "[O]");
    }
}