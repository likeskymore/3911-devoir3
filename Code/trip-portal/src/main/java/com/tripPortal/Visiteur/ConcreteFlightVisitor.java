package com.tripPortal.Visiteur;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ConcreteFlightVisitor implements Visitor {

	// YUL-YYZ:[AIRCAN]AC481(2014.11.28:06.00-2014.11.28:07:24)|PS(0/12)474.00|AM(5/16)355.50
	@Override
	public String visit(JsonNode node) {
		String origin = node.has("origin") ? node.get("origin").asText() : "?";
		String destination = node.has("destination") ? node.get("destination").asText() : "?";
		String company = node.has("company") ? node.get("company").asText() : "?";
		String id = node.has("id") ? node.get("id").asText() : "?";
		String start = node.has("startDate") ? node.get("startDate").asText().replace("-", ".") : "?";
		String end = node.has("endDate") ? node.get("endDate").asText().replace("-", ".") : "?";
		String transportID = node.has("transport") ? node.get("transport").asText() : null;
		float price = node.has("price") ? (float) node.get("price").asDouble() : 0;

		StringBuilder sb = new StringBuilder();
		sb.append(origin).append("-").append(destination)
				.append(" : [").append(company).append("] ")
				.append(id)
				.append(" (").append(start).append("-").append(end).append(") ");

		// ── Lire les sections depuis Transport.json ────────────────
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
							String sectionType = section.has("sectionType")
									? section.get("sectionType").asText()
									: "?";
							String layout = section.has("layout")
									? section.get("layout").asText()
									: "?";

							// Compter sièges occupés et total
							int total = 0;
							int occupied = 0;
							if (section.has("seats")) {
								for (JsonNode seat : section.get("seats")) {
									total++;
									if (seat.has("occupied") && seat.get("occupied").asBoolean()) {
										occupied++;
									}
								}
							}

							// Format : PS(0/12)474.00
							// sectionType + layout + (occupés/total) + prix
							sb.append(" | ")
									.append(sectionType).append(layout)
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
	public String visitRoute(JsonNode node) {
		return "";
	}

	@Override
	public String visitCruiseLine(JsonNode node) {
		return "";
	}
}