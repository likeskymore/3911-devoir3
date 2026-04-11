package com.tripPortal.Mediateur;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Reservation;
//import com.sun.jdi.connect.Transport;
import com.tripPortal.Model.Seat;
import com.tripPortal.Model.Section;

public class reservationController {

	/**
	 * 
	 * @param trip
	 * @param transportID
	 * @param seat
	 */
	public int reserveTrip(JsonNode tripNode, String seatID) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode transportRoot = mapper.readTree(new File("src/Database/Transport.json"));

			// ── Trouver le transport dans le JSON ──────────────────────
			String transportID = tripNode.get("transport").asText();
			JsonNode transportNode = null;
			for (JsonNode node : transportRoot) {
				if (node.has("transportID") && node.get("transportID").asText().equals(transportID)) {
					transportNode = node;
					break;
				}
			}
			if (transportNode == null) {
				System.err.println("Transport not found: " + transportID);
				return -1;
			}

			// ── Trouver le siège directement dans le JSON ──────────────
			String type = tripNode.get("type").asText();
			String seatsKey = type.equals("CruiseLine") ? "cabins" : "seats";
			JsonNode seatNode = null;

			if (transportNode.has("sections")) {
				for (JsonNode section : transportNode.get("sections")) {
					if (!section.has(seatsKey)) continue;
					for (JsonNode seat : section.get(seatsKey)) {
						if (seat.has("seatID") && seat.get("seatID").asText().equals(seatID)) {
							seatNode = seat;
							break;
						}
					}
					if (seatNode != null) break;
				}
			}

			if (seatNode == null) {
				System.err.println("Seat not found: " + seatID);
				return -2;
			}
			if (seatNode.has("occupied") && seatNode.get("occupied").asBoolean()) {
				System.err.println("Seat already occupied: " + seatID);
				return -3;
			}

			// ── Récupérer le trip ──────────────────────────────────────
			String tripId = tripNode.get("id").asText();

			// ── Créer la réservation ───────────────────────────────────
			Reservation reservation = new Reservation(transportID, seatID, tripId, false);
			saveReservation(reservation, mapper);

			// ── Mettre à jour le siège dans Transport.json ─────────────
			((ObjectNode) seatNode).put("occupied", true);
			mapper.writerWithDefaultPrettyPrinter()
				.writeValue(new File("src/Database/Transport.json"), transportRoot);

			return 0; // succès

		} catch (IOException e) {
			System.err.println("Failed to reserve trip: " + e.getMessage());
			return -4;
		}
	}

	private void saveReservation(Reservation reservation, ObjectMapper mapper) throws IOException {
		File file = new File("src/Database/User.json");
		JsonNode root = mapper.readTree(file);
		ArrayNode array = root.isArray() ? (ArrayNode) root : mapper.createArrayNode();

		ObjectNode node = mapper.createObjectNode();
		node.put("reservationNumber", reservation.getReservationNumber());
		node.put("tripId", reservation.getTripId());
		node.put("transportID", reservation.getTransportID());
		node.put("seatID", reservation.getReservedSeat());
		node.put("confirmed", reservation.isPaid());

		array.add(node);
		mapper.writerWithDefaultPrettyPrinter().writeValue(file, array);
	}

}