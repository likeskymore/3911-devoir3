package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ConcreteCruiseLineVisitor implements Visitor {

    // Miami-Barcelona-Venice:[MSC]XIPOUD(2026-04-01-2026-04-16)|I(0/10)4|O(0/5)2
    @Override
    public String visitCruiseLine(JsonNode node) {
        String path        = buildPath(node);
        String company     = node.has("company")     ? node.get("company").asText()          : "?";
        String id          = node.has("id")          ? node.get("id").asText()               : "?";
        String start       = node.has("startDate")   ? node.get("startDate").asText()        : "?";
        String end         = node.has("endDate")     ? node.get("endDate").asText()          : "?";
        String transportID = node.has("transport")   ? node.get("transport").asText()        : null;
        float price        = node.has("price")       ? (float) node.get("price").asDouble()  : 0;

        StringBuilder sb = new StringBuilder();
        sb.append(path)
          .append(" : [").append(company).append("] ")
          .append(id)
          .append(" (").append(start).append("-").append(end).append(") ");

        if (transportID != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode transportRoot = mapper.readTree(new File("src/Database/Transport.json"));

                for (JsonNode transport : transportRoot) {
                    if (!transport.has("transportID")) continue;
                    if (!transport.get("transportID").asText().equals(transportID)) continue;

                    if (transport.has("sections")) {
                        for (JsonNode section : transport.get("sections")) {
                            String sectionType = section.has("sectionType") 
                                ? section.get("sectionType").asText() : "?";

                            int total = 0, occupied = 0;
                            if (section.has("cabins")) {
                                for (JsonNode cabin : section.get("cabins")) {
                                    total++;
                                    if (cabin.has("occupied") && cabin.get("occupied").asBoolean())
                                        occupied++;
                                }
                            }
                            // Format : I(0/10)cap4
                            sb.append(" | ")
                              .append(sectionType)
                              .append(" (").append(occupied).append("/").append(total).append(") ")
                              .append(price);
                        }
                    }
                    break;
                }
            } catch (IOException e) {
                System.err.println("Failed to read Transport.json: " + e.getMessage());
            }
        }

        return sb.toString();
    }

    @Override
    public String visit(JsonNode node) { return ""; }

    @Override
    public String visitRoute(JsonNode node) { return ""; }

    private String buildPath(JsonNode node) {
        if (!node.has("path")) return "?";
        StringBuilder sb = new StringBuilder();
        for (JsonNode stop : node.get("path")) {
            if (sb.length() > 0) sb.append("-");
            sb.append(stop.asText());
        }
        return sb.toString();
    }
}