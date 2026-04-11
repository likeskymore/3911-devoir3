package com.tripPortal.Model;

public class TrainSeat extends Seat {

    private int row;       // numéro de rangée
    private char column;   // lettre de colonne A-C (disposition Étroit)

    public TrainSeat(int row, char column) {
        super("T" + row + column); // ex: "T5B"
        this.row = row;
        this.column = column;
    }

    /** Format : "T" + rangée + colonne → ex: "T5B" */
    @Override
    public String getSeatID() {
        return "T" + row + column;
    }

    public int getRow()     { return row; }
    public char getColumn() { return column; }
}