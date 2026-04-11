package com.tripPortal.Model;

import java.util.ArrayList;

public class SectionBoat extends Section {

    public enum SectionBoatType {
        I(4),  // Intérieure       : jusqu'à 4 personnes
        O(2),  // Vue sur l'Océan  : jusqu'à 2 personnes
        S(5),  // Suite            : jusqu'à 5 personnes
        F(6),  // Famille          : jusqu'à 6 personnes
        D(6);  // Famille Deluxe   : jusqu'à 6 personnes

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