package com.tripPortal.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class Transport {
    String name;
    String TransportID;
    ArrayList<Section> sections;
    boolean available;

    public Transport(String name) {
        this.name = name;
        this.TransportID = randomGenerateID();
        this.available = true;
        this.sections = new ArrayList<>();
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

    public static Transport fromJson(String transportID, JsonNode root) {
        for (JsonNode node : root) {
            if (node.get("transportID").asText().equals(transportID)) {
                String type = node.get("type").asText();
                return switch (type) {
                    case "Plane" -> new Plane(node);
                    case "Boat"  -> new Boat(node);
                    case "Train" -> new Train(node);
                    default -> throw new IllegalArgumentException("Unknown transport type: " + type);
                };
            }
        }
        throw new IllegalArgumentException("Transport not found: " + transportID);
    }
}
