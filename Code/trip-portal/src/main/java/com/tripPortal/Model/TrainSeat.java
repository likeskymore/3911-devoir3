package com.tripPortal.Model;

public class TrainSeat extends Seat {

    private SectionTrain.SectionTrainType sectionType;
    private int row;
    private char column;

    public TrainSeat(SectionTrain.SectionTrainType sectionType, int row, char column) {
        super(sectionType.name() + row + column); // e.g. "P5B", "E12A"
        this.sectionType = sectionType;
        this.row = row;
        this.column = column;
    }

    @Override
    public String getSeatID() {
        return sectionType.name() + row + column; // "P5B", "E12A"
    }

    @Override
    public float calculatePrice(float basePrice) {
        if (sectionType == SectionTrain.SectionTrainType.P)
            return basePrice * 0.60f;
        if (sectionType == SectionTrain.SectionTrainType.E)
            return basePrice * 0.50f;
        return basePrice;
    }

    public SectionTrain.SectionTrainType getSectionType() {
        return sectionType;
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }
}