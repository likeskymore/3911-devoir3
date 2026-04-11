package com.tripPortal.Model;

import java.util.ArrayList;

public class SectionTrain extends Section {

    // Seulement P (Première) et E (Économie) pour le train
    public enum SectionTrainType { P, E }

    // Disposition toujours Étroit (S) : 3 colonnes, aile entre 1 et 2
    private static final int COLUMNS = 3;

    private SectionTrainType sectionType;
    private int numberOfRows;

    public SectionTrain(SectionTrainType sectionType, int numberOfRows) {
        super(sectionType.name());
        if (numberOfRows < 1 || numberOfRows > 100)
            throw new IllegalArgumentException("Le nombre de rangées doit être entre 1 et 100.");

        this.sectionType = sectionType;
        this.numberOfRows = numberOfRows;
        this.seats = new ArrayList<>();
        generateSeats();
    }

    @Override
    protected void generateSeats() {
        for (int row = 1; row <= numberOfRows; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char colLetter = (char) ('A' + col); // A, B, C
                seats.add(new TrainSeat(row, colLetter));
            }
        }
    }

    public SectionTrainType getSectionType() { return sectionType; }
    public int getNumberOfRows()             { return numberOfRows; }
    public int getNumberOfColumns()          { return COLUMNS; }

    @Override
    public String toString() {
        return "SectionTrain[" + sectionType + ", layout=S(Étroit)"
                + ", rangées=" + numberOfRows
                + ", sièges=" + getTotalSeatsCount() + "]";
    }
}