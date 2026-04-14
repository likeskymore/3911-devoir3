package com.tripPortal.Model;

import java.util.ArrayList;
import java.util.List;

public class SectionPlane extends Section {

    public enum SectionPlaneType {F, A, P, E}

    public enum Layout {S,C,M,L }

    private SectionPlaneType sectionType;
    private Layout layout;
    private int numberOfRows; // max 100

    public SectionPlane(SectionPlaneType sectionType, Layout layout, int numberOfRows) {
        super(sectionType.name());
        if (numberOfRows < 1 || numberOfRows > 100)
            throw new IllegalArgumentException("Le nombre de rangées doit être entre 1 et 100.");

        this.sectionType = sectionType;
        this.layout = layout;
        this.numberOfRows = numberOfRows;
        this.seats = new ArrayList<>();
        generateSeats();
    }

    @Override
    protected void generateSeats() {
        int numCols = getNumberOfColumns();
        for (int row = 1; row <= numberOfRows; row++) {
            for (int col = 0; col < numCols; col++) {
                char colLetter = (char) ('A' + col);
                seats.add(new PlaneSeat(sectionType, row, colLetter));
            }
        }
    }

    public int getNumberOfColumns() {
        return switch (layout) {
            case S -> 3;
            case C -> 4;
            case M -> 6;
            case L -> 10;
        };
    }

    public SectionPlaneType getSectionType() {
        return sectionType;
    }

    public void setSectionType(SectionPlaneType sectionType) {
        this.sectionType = sectionType;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    @Override
    public String toString() {
        return "SectionPlane[" + sectionType + ", layout=" + layout
                + ", rangées=" + numberOfRows
                + ", sièges=" + seats.size() + "]";
    }
}