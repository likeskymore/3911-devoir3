package com.tripPortal.Model;

import java.util.ArrayList;

public class SectionBoat extends Section {

    public enum SectionBoatType {
        I(4),
        O(2),
        S(5),
        F(6),
        D(6);

        private final int maxCapacity;

        SectionBoatType(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }

        public int getMaxCapacity() { return maxCapacity; }
    }

    private SectionBoatType sectionType;
    private int numberOfCabins;

    public SectionBoat(SectionBoatType sectionType, int numberOfCabins) {
        super(sectionType.name());
        if (numberOfCabins < 1)
            throw new IllegalArgumentException("Le nombre de cabines doit être au moins 1.");

        this.sectionType = sectionType;
        this.numberOfCabins = numberOfCabins;
        this.seats = new ArrayList<>();
        generateSeats();
    }

    @Override
    protected void generateSeats() {
        for (int i = 1; i <= numberOfCabins; i++) {
            seats.add(new BoatCabin(sectionType, i));
        }
    }

    public SectionBoatType getSectionType() { return sectionType; }
    public int getNumberOfCabins()          { return numberOfCabins; }

    @Override
    public String toString() {
        return "SectionBoat[" + sectionType
                + ", capacité/cabine=" + sectionType.getMaxCapacity()
                + ", cabines=" + numberOfCabins + "]";
    }
}