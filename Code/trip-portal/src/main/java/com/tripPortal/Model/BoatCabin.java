package com.tripPortal.Model;

public class BoatCabin extends Seat {

    private SectionBoat.SectionBoatType sectionType;
    private int cabinNumber;
    private int capacity;  // nombre max de personnes selon le type

    public BoatCabin(SectionBoat.SectionBoatType sectionType, int cabinNumber) {
        super(sectionType.name() + "-C" + cabinNumber); // ex: "I-C5", "S-C12"
        this.sectionType = sectionType;
        this.cabinNumber = cabinNumber;
        this.capacity = sectionType.getMaxCapacity();
    }

    @Override
    public float calculatePrice(float basePrice) {
        switch (sectionType) {
            case D: return basePrice * 1.00f;
            case F: return basePrice * 0.90f;
            case S: return basePrice * 0.90f;
            case O: return basePrice * 0.75f;
            case I: return basePrice * 0.50f;
            default: return basePrice;
        }
    }

    @Override
    public String getSeatID() {
        return sectionType.name() + "-C" + cabinNumber;
    }

    public SectionBoat.SectionBoatType getSectionType() { return sectionType; }
    public int getCabinNumber()                         { return cabinNumber; }
    public int getCapacity()                            { return capacity; }
}