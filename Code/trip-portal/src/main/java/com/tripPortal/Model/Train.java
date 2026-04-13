package com.tripPortal.Model;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;

public class Train extends Transport {

    private static final int DEFAULT_ROWS_PREMIERE = 10;
    private static final int DEFAULT_ROWS_ECONOMIE = 30;

    public Train(String name) {
        super(name);
        this.sections = new ArrayList<>();
        initSections();
    }

    public Train(String id, String placeholder) {
        super(id, placeholder);
    }

    public Train(JsonNode node) {
        super(node.get("name").asText());
        this.TransportID = node.get("transportID").asText();
        this.sections = new ArrayList<>();
        initSections();
    }

    /**
     * Tout train contient obligatoirement une section Première (P)
     * et une section Économie (E), toutes deux en disposition Étroit (S).
     */
    private void initSections() {
        sections.add(new SectionTrain(SectionTrain.SectionTrainType.P, DEFAULT_ROWS_PREMIERE));
        sections.add(new SectionTrain(SectionTrain.SectionTrainType.E, DEFAULT_ROWS_ECONOMIE));
    }
}