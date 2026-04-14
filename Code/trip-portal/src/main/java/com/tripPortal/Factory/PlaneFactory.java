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
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Seat;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.TransportPrototype;
import com.tripPortal.Model.Trip;

public class PlaneFactory extends PlaneTripFactory {

	// Singleton
    private static PlaneFactory instance;

    private PlaneFactory() {}

    public static PlaneFactory getInstance() {
        if (instance == null) {
            instance = new PlaneFactory();
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
    public Plane createTransport(String name, String companyName, List<SectionPlane> sections) {

    // ── 1. Créer l'objet Java ──────────────────────────────────────
    Plane plane = new Plane(name);
    for (SectionPlane section : sections) {
        plane.addSection(section);
    }

    // ── 2. Sauvegarder en JSON ─────────────────────────────────────
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
        node.put("transportID", plane.getTransportID());
        node.put("type", "Plane");
        node.put("name", name);
        node.put("company", companyName);
        node.put("available", plane.isAvailable());

        // ── Sections ───────────────────────────────────────────────
        ArrayNode sectionsArray = mapper.createArrayNode();
        for (SectionPlane sp : sections) {
            ObjectNode secNode = mapper.createObjectNode();
            secNode.put("sectionType", sp.getSectionType().name());
            secNode.put("layout", sp.getLayout().name());
            secNode.put("numberOfRows", sp.getNumberOfRows());
            secNode.put("numberOfColumns", sp.getNumberOfColumns());

            // ── Sièges ─────────────────────────────────────────────
            ArrayNode seatsArray = mapper.createArrayNode();
            for (Seat seat : sp.getSeats()) {
                ObjectNode seatNode = mapper.createObjectNode();
                seatNode.put("seatID", seat.getSeatID());
                seatNode.put("occupied", seat.isOccupied());
                seatsArray.add(seatNode);
            }
            secNode.set("seats", seatsArray);
            sectionsArray.add(secNode);
        }
        node.set("sections", sectionsArray);

        array.add(node);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);

    } catch (IOException e) {
        System.err.println("Failed to write Plane to JSON: " + e.getMessage());
        e.printStackTrace();
    }

    return plane;
}

}