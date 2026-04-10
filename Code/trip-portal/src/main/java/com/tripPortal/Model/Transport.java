package com.tripPortal.Model;

import java.util.ArrayList;
import java.util.Random;

public abstract class Transport {
    String TransportID;
    ArrayList<Section> sections;
    boolean available;

    public Transport(boolean available) {
        this.TransportID = randomGenerateID();
        this.available = available;
    }

    private String randomGenerateID() {
        String id = "";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int number = (rand.nextInt(9));
            id += number;
        }
        return id;
    }

    public String getTransportID() {
        return TransportID;
    }

    public void setTransportID(String transportID) {
        TransportID = transportID;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
