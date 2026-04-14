package com.tripPortal.Visiteur;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConcreteCruiseLineVisitor implements Visitor {

    @Override
    public String visitCruiseLine(JsonNode node) {
        String path = buildPath(node);
        String company = node.has("company") ? node.get("company").asText() : "?";
        String id = node.has("id") ? node.get("id").asText() : "?";
        String start = node.has("startDate") ? node.get("startDate").asText() : "?";
        String end = node.has("endDate") ? node.get("endDate").asText() : "?";
        String transportID = node.has("transport") ? node.get("transport").asText() : null;
        float price = node.has("price") ? (float) node.get("price").asDouble() : 0;

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
                    if (!transport.has("transportID"))
                        continue;
                    if (!transport.get("transportID").asText().equals(transportID))
                        continue;

                    if (transport.has("sections")) {
                        for (JsonNode section : transport.get("sections")) {
                            String sectionType = section.has("sectionType") ? section.get("sectionType").asText() : "?";

                            int total = 0, occupied = 0;
                            if (section.has("cabins")) {
                                for (JsonNode cabin : section.get("cabins")) {
                                    total++;
                                    if (!"Available".equals(cabin.path("state").asText("Available")))
                                        occupied++;
                                }
                            }

                            float sectionPrice = price;
                            switch (sectionType) {
                                case "D":
                                    sectionPrice = price * 1.00f;
                                    break;
                                case "F":
                                    sectionPrice = price * 0.90f;
                                    break;
                                case "S":
                                    sectionPrice = price * 0.90f;
                                    break;
                                case "O":
                                    sectionPrice = price * 0.75f;
                                    break;
                                case "I":
                                    sectionPrice = price * 0.50f;
                                    break;
                            }

                            sb.append(" | ")
                                    .append(sectionType)
                                    .append(" (").append(occupied).append("/").append(total).append(") ")
                                    .append(String.format("%.2f", sectionPrice));
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
    public String visit(JsonNode node) {
        return "";
    }

    @Override
    public String visitRoute(JsonNode node) {
        return "";
    }

    private String buildPath(JsonNode node) {
        if (!node.has("path"))
            return "?";
        StringBuilder sb = new StringBuilder();
        for (JsonNode stop : node.get("path")) {
            if (sb.length() > 0)
                sb.append("-");
            sb.append(stop.asText());
        }
        return sb.toString();
    }
}