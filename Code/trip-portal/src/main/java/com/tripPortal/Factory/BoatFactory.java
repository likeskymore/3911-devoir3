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
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Seat;
import com.tripPortal.Model.SectionBoat;
import com.tripPortal.Model.TransportPrototype;
import com.tripPortal.Model.Trip;

public class BoatFactory extends PlaneTripFactory {

	// Singleton
    private static BoatFactory instance;

    private BoatFactory() {}

    public static BoatFactory getInstance() {
        if (instance == null) {
            instance = new BoatFactory();
        }
        return instance;
    }

	// Patron de fabrique
	public Location createLocation(String city) {
		return null;
	}
	public Company createCompany(String name){
		return null;
	}
    public Trip createTrajectory(
        Company company,
        LocalDate startDate,
        LocalDate endDate,
        float price,
        int duration,
        ArrayList<Location> locations,
        TransportPrototype transport
    ){
		return null;
	}
    public TransportPrototype createTransport(String name, String companyName, List<SectionBoat> sections) {

        // Créer l'objet Java
        Boat boat = new Boat(name);
        for (SectionBoat section : sections) {
            boat.addSection(section);
        }

        // Sauvegarder en JSON 
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
            node.put("transportID", boat.getTransportID());
            node.put("type", "Boat");
            node.put("name", name);
            node.put("company", companyName);
            node.put("available", boat.isAvailable());

            // ── Sections ───────────────────────────────────────────────
            ArrayNode sectionsArray = mapper.createArrayNode();
            for (SectionBoat sb : sections) {
                ObjectNode secNode = mapper.createObjectNode();
                secNode.put("sectionType", sb.getSectionType().name());
                secNode.put("maxCapacity", sb.getSectionType().getMaxCapacity());
                secNode.put("numberOfCabins", sb.getNumberOfCabins());

                // ── Cabines ────────────────────────────────────────────
                ArrayNode seatsArray = mapper.createArrayNode();
                for (Seat seat : sb.getSeats()) {
                    ObjectNode seatNode = mapper.createObjectNode();
                    seatNode.put("seatID", seat.getSeatID());
                    seatNode.put("state", seat.getStateName());
                    seatsArray.add(seatNode);
                }
                secNode.set("cabins", seatsArray);
                sectionsArray.add(secNode);
            }
            node.set("sections", sectionsArray);

            array.add(node);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

        } catch (IOException e) {
            System.err.println("Failed to write Boat to JSON: " + e.getMessage());
            e.printStackTrace();
        }

    return boat;
}

}