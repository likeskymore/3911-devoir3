package com.tripPortal.Model;

public class PlaneSeat extends Seat {
    private SectionPlane.SectionPlaneType sectionType; // F, A, P, E
    private int row; // 1 - 100
    private char column; // A - J
    private boolean occupied;

    public PlaneSeat(SectionPlane.SectionPlaneType sectionType, int row, char column) {
        super(sectionType.name() + row + column);
        this.sectionType = sectionType;
        this.row = row;
        this.column = column;
    }

    // Ex: "F12A" → section Première, rangée 12, colonne A
    public String getSeatID() {
        return sectionType.name() + row + column;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public SectionPlane.SectionPlaneType getSectionType() {
        return sectionType;
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    @Override
    public float calculatePrice(float basePrice) {
        switch (sectionType) {
            case F:
                return basePrice * 1.00f;
            case A:
                return basePrice * 0.75f;
            case P:
                return basePrice * 0.60f;
            case E:
                return basePrice * 0.50f;
            default:
                return basePrice;
        }
    }

    @Override
    public String toString() {
        return getSeatID() + (occupied ? "[X]" : "[O]");
    }
}