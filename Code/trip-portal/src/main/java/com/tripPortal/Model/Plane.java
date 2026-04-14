package com.tripPortal.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class Plane extends TransportPrototype {

    public Plane(String name) {
        super(name);
    }

    public Plane(JsonNode node) {
        super(node.get("name").asText());
        this.TransportID = node.get("transportID").asText();
    }

    public Plane(String id, String placeholder) {
        super(id, placeholder);
    }

    public boolean addSection(Section section) {
        boolean alreadyExists = sections.stream()
                .anyMatch(s -> s.getSectionName().equals(section.getSectionName()));
        if (alreadyExists) {
            System.err.println("La section " + section.getSectionName() + " existe déjà.");
            return false;
        }
        sections.add(section);
        return true;
    }
}
