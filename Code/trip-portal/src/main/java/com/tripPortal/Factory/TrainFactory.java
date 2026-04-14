package com.tripPortal.Factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Seat;
import com.tripPortal.Model.SectionTrain;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;

public class TrainFactory extends PlaneTripFactory {

    // Singleton
    private static TrainFactory instance;

    private TrainFactory() {
    }

    public static TrainFactory getInstance() {
        if (instance == null) {
            instance = new TrainFactory();
        }
        return instance;
    }

    // Patron de fabrique
    public Location createLocation(String city) {
        return null;
    }

    public Company createCompany(String name) {
        return null;
    }

    public Trip createTrajectory(
            Company company,
            LocalDate startDate,
            LocalDate endDate,
            float price,
            int duration,
            ArrayList<Location> locations,
            Transport transport) {
        return null;
    }

    public Transport createTransport(String name, String companyName, List<SectionTrain> sections) {

        // Créer l'objet Java
        Train train = new Train(name);
        for (SectionTrain section : sections) {
            train.getSections().clear();
        }
        for (SectionTrain section : sections) {
            train.getSections().add(section);
        }

        //Sauvegarder en JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("src/Database/Transport.json");

            JsonNode root = mapper.readTree(file);
            ArrayNode array;

            if (root == null || root.isMissingNode() || root.isNull()) {
                array = mapper.createArrayNode(); // fichier vide ou null
            } else if (root.isArray()) {
                array = (ArrayNode) root;
            } else {
                throw new IOException("Transport.json doit contenir un tableau JSON []");
            }

            ObjectNode node = mapper.createObjectNode();
            node.put("transportID", train.getTransportID());
            node.put("type", "Train");
            node.put("name", name);
            node.put("company", companyName);
            node.put("available", train.isAvailable());

            // ── Sections ───────────────────────────────────────────────
            ArrayNode sectionsArray = mapper.createArrayNode();
            for (SectionTrain st : sections) {
                ObjectNode secNode = mapper.createObjectNode();
                secNode.put("sectionType", st.getSectionType().name());
                secNode.put("layout", "S"); // toujours Étroit
                secNode.put("numberOfRows", st.getNumberOfRows());
                secNode.put("numberOfColumns", 3); // toujours 3

                // ── Sièges ─────────────────────────────────────────────
                ArrayNode seatsArray = mapper.createArrayNode();
                for (Seat seat : st.getSeats()) {
                    ObjectNode seatNode = mapper.createObjectNode();
                    seatNode.put("seatID", seat.getSeatID());
                    seatNode.put("state", seat.getStateName());
                    seatsArray.add(seatNode);
                }
                secNode.set("seats", seatsArray);
                sectionsArray.add(secNode);
            }
            node.set("sections", sectionsArray);

            array.add(node);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

        } catch (IOException e) {
            System.err.println("Failed to write Train to JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return train;
    }

}