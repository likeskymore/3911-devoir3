package com.tripPortal.Model;

import com.tripPortal.Etat.SeatState;

public abstract class Seat {

    protected String id; // identifiant unique du siège (ex: "F12A", "Cabin-3", "2A")
    protected SeatState currentState;
    protected boolean occupied;

    public Seat(String id) {
        this.id = id;
        this.occupied = false;
    }

    // ── Méthode abstraite ─────────────────────────────────────────

    /**
     * Retourne l'identifiant lisible du siège — chaque sous-classe définit son
     * format
     */
    public abstract String getSeatID();

    // ── Méthodes communes ─────────────────────────────────────────

    public void setState(SeatState s) {
        this.currentState = s;
    }

    // public void event(String e) {
    // if (currentState != null) {
    // currentState.handle(e, this); // délègue à l'état courant
    // }
    // }

    // ── Getters / Setters ─────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public SeatState getCurrentState() {
        return currentState;
    }

    public abstract float calculatePrice(float basePrice);

    @Override
    public String toString() {
        return getSeatID() + (occupied ? "[X]" : "[O]");
    }
}