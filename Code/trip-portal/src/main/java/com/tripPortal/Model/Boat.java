package com.tripPortal.Model;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;

public class Boat extends Transport {

    public Boat(String name) {
        super(name);
        this.sections = new ArrayList<>();
    }

    public Boat(JsonNode node) {
        super(node.get("name").asText());
        this.TransportID = node.get("transportID").asText();
        this.sections = new ArrayList<>();
    }

    /**
     * Un paquebot peut avoir les 5 sections — on vérifie les doublons via sectionName.
     * Le nombre de cabines varie par paquebot, donc passé en paramètre.
     */
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