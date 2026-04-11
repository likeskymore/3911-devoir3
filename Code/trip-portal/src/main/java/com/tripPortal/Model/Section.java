package com.tripPortal.Model;

import java.util.List;

public abstract class Section {

    protected String sectionName;
    protected List<Seat> seats;

    public Section(String sectionName) {
        this.sectionName = sectionName;
    }

    // ── Méthodes communes ─────────────────────────────────────────

    /** Génère les sièges — chaque sous-classe définit sa propre logique */
    protected abstract void generateSeats();

    /** Retourne le nombre total de sièges disponibles (non occupés) */
    public long getAvailableSeatsCount() {
        return seats.stream().filter(s -> !s.isOccupied()).count();
    }

    /** Retourne le nombre total de sièges */
    public int getTotalSeatsCount() {
        return seats.size();
    }

    /** Cherche un siège par son ID (ex: "F12A") */
    public Seat findSeatById(String seatId) {
        return seats.stream()
                .filter(s -> s.getSeatID().equals(seatId))
                .findFirst()
                .orElse(null);
    }

    /** Réserve un siège — retourne false si déjà occupé ou introuvable */
    public boolean bookSeat(String seatId) {
        Seat seat = findSeatById(seatId);
        if (seat == null || seat.isOccupied()) return false;
        seat.setOccupied(true);
        return true;
    }

    /** Libère un siège */
    public boolean releaseSeat(String seatId) {
        Seat seat = findSeatById(seatId);
        if (seat == null || !seat.isOccupied()) return false;
        seat.setOccupied(false);
        return true;
    }

    // ── Getters / Setters ─────────────────────────────────────────

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public List<Seat> getSeats() { return seats; }

    @Override
    public String toString() {
        return "Section[" + sectionName
                + ", total=" + getTotalSeatsCount()
                + ", disponibles=" + getAvailableSeatsCount() + "]";
    }
}