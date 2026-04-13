package com.tripPortal.Model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Factory.TripFactory;

public abstract class Trip {

	private String id;
	private Company servicedBy;
	private LocalDate departureTime;
	private LocalDate arrivalTime;
	private float price;
	private int tripDuration;
	private TripFactory tripFactory;
	private Boolean active;

	public Trip(Company servicedBy, LocalDate departureTime, LocalDate arrivalTime, float price, int tripDuration) {
		this.id = randomGenerateID(servicedBy);
		this.servicedBy = servicedBy;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.price = price;
		this.tripDuration = tripDuration;
		this.active = true;
	}

	public Trip(String id) {
		this.id = id;
	}

	private String randomGenerateID(Company servicedBy) {
		String id = servicedBy.getTripID();
		Random rand = new Random();

		for (int i = 0; i < 4; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// a complete

		return id;

	}

	public String getId() {
		return id;
	}

	public Company getServicedBy() {
		return servicedBy;
	}

	public LocalDate getDepartureTime() {
		return departureTime;
	}

	public LocalDate getArrivalTime() {
		return arrivalTime;
	}

	public float getPrice() {
		return price;
	}

	public int getTripDuration() {
		return tripDuration;
	}

	public void delete() throws IOException {
		ObjectMapper displayMapper = new ObjectMapper();
		File tripFile = new File("src/Database/Trip.json");
		ArrayNode tripArray = (ArrayNode) displayMapper.readTree(tripFile);
		for (int i = 0; i < tripArray.size(); i++) {
			if (tripArray.get(i).get("id").asText().equals(this.id)) {
				tripArray.remove(i);
				break;
			}
		}
		try {
			displayMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, tripArray);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			ObjectMapper cm = new ObjectMapper();
			File cf = new File("src/Database/Company.json");
			ArrayNode ca = (ArrayNode) cm.readTree(cf);
			for (JsonNode cn : ca) {
				ArrayNode tn = (ArrayNode) cn.get("Trips");
				for (int i = 0; i < tn.size(); i++) {
					if (tn.get(i).asText().equals(this.id)) {
						tn.remove(i);
						break;
					}
				}
			}
			cm.writerWithDefaultPrettyPrinter().writeValue(cf, ca);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void updatePrice(String newPrice) {
		try {
			ObjectMapper m = new ObjectMapper();
			File f = new File("src/Database/Trip.json");
			ArrayNode arr = (ArrayNode) m.readTree(f);
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).get("id").asText().equals(this.id)) {
					((ObjectNode) arr.get(i)).put("price", newPrice);
					break;
				}
			}
			m.writerWithDefaultPrettyPrinter().writeValue(f, arr);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void updateTrip(String newCompany, LocalDate newDepartureTime, LocalDate newArrivalTime,
			float newPrice, String newTransportId) {
		if (newCompany == null || newCompany.isBlank()) {
			throw new IllegalArgumentException("Company is required.");
		}
		if (newDepartureTime == null || newArrivalTime == null) {
			throw new IllegalArgumentException("Both start and end dates are required.");
		}
		if (newArrivalTime.isBefore(newDepartureTime)) {
			throw new IllegalArgumentException("End date must be on or after the start date.");
		}
		if (newTransportId == null || newTransportId.isBlank()) {
			throw new IllegalArgumentException("Transport is required.");
		}

		try {
			ObjectMapper mapper = new ObjectMapper();

			File tripFile = new File("src/Database/Trip.json");
			ArrayNode trips = readArray(mapper, tripFile);
			ObjectNode tripNode = findNodeById(trips, "id", this.id);
			if (tripNode == null) {
				throw new IllegalStateException("Trip not found: " + this.id);
			}

			String oldCompany = tripNode.path("company").asText("");
			String oldTransportId = tripNode.path("transport").asText("");
			String tripType = tripNode.path("type").asText("");

			File companyFile = new File("src/Database/Company.json");
			ArrayNode companies = readArray(mapper, companyFile);
			ObjectNode newCompanyNode = findNodeByName(companies, newCompany);
			if (newCompanyNode == null) {
				throw new IllegalArgumentException("Company not found: " + newCompany);
			}
			String expectedCompanyType = expectedCompanyType(tripType);
			if (expectedCompanyType != null && !expectedCompanyType.equals(newCompanyNode.path("type").asText(""))) {
				throw new IllegalArgumentException("Company type does not match this trip type.");
			}

			File transportFile = new File("src/Database/Transport.json");
			ArrayNode transports = readArray(mapper, transportFile);
			ObjectNode newTransportNode = findNodeById(transports, "transportID", newTransportId);
			if (newTransportNode == null) {
				throw new IllegalArgumentException("Transport not found: " + newTransportId);
			}
			String expectedTransportType = expectedTransportType(tripType);
			if (expectedTransportType != null
					&& !expectedTransportType.equals(newTransportNode.path("type").asText(""))) {
				throw new IllegalArgumentException("Transport type does not match this trip type.");
			}
			if (!newCompany.equals(newTransportNode.path("company").asText(""))) {
				throw new IllegalArgumentException("Selected transport must belong to the selected company.");
			}

			boolean transportChanged = !oldTransportId.equals(newTransportId);
			ArrayNode userReservations = null;
			ObjectNode oldTransportNode = null;
			if (transportChanged) {
				oldTransportNode = findNodeById(transports, "transportID", oldTransportId);
				if (oldTransportNode == null) {
					throw new IllegalStateException("Original transport not found: " + oldTransportId);
				}

				File userFile = new File("src/Database/User.json");
				userReservations = readArray(mapper, userFile);
				String seatsKey = seatsKey(newTransportNode);
				for (JsonNode reservation : userReservations) {
					if (!this.id.equals(reservation.path("tripId").asText(""))) {
						continue;
					}
					String seatId = reservation.path("seatID").asText("");
					if (seatId.isBlank()) {
						continue;
					}
					ObjectNode seatNode = findSeatNode(newTransportNode, seatsKey, seatId);
					if (seatNode == null) {
						throw new IllegalArgumentException(
								"Seat " + seatId + " is not available on the selected transport.");
					}
					if (seatNode.path("occupied").asBoolean(false)) {
						throw new IllegalArgumentException(
								"Seat " + seatId + " is already occupied on the selected transport.");
					}
				}
			}

			tripNode.put("company", newCompany);
			tripNode.put("startDate", newDepartureTime.toString());
			tripNode.put("endDate", newArrivalTime.toString());
			tripNode.put("duration", (int) ChronoUnit.DAYS.between(newDepartureTime, newArrivalTime));
			tripNode.put("price", newPrice);
			tripNode.put("transport", newTransportId);

			if (!oldCompany.equals(newCompany)) {
				updateCompanyTrips(companies, oldCompany, newCompany, this.id);
				mapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
			}

			if (transportChanged) {
				File userFile = new File("src/Database/User.json");
				if (userReservations == null) {
					userReservations = readArray(mapper, userFile);
				}

				String oldSeatsKey = seatsKey(oldTransportNode);
				String newSeatsKey = seatsKey(newTransportNode);
				for (JsonNode reservation : userReservations) {
					if (!this.id.equals(reservation.path("tripId").asText(""))) {
						continue;
					}

					String seatId = reservation.path("seatID").asText("");
					if (seatId.isBlank()) {
						continue;
					}

					ObjectNode oldSeat = findSeatNode(oldTransportNode, oldSeatsKey, seatId);
					if (oldSeat == null) {
						throw new IllegalStateException("Seat " + seatId + " was not found on the original transport.");
					}
					oldSeat.put("occupied", false);

					ObjectNode newSeat = findSeatNode(newTransportNode, newSeatsKey, seatId);
					if (newSeat == null) {
						throw new IllegalStateException("Seat " + seatId + " was not found on the selected transport.");
					}
					newSeat.put("occupied", true);

					((ObjectNode) reservation).put("transportID", newTransportId);
				}

				mapper.writerWithDefaultPrettyPrinter().writeValue(userFile, userReservations);
				mapper.writerWithDefaultPrettyPrinter().writeValue(transportFile, transports);
			}

			mapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
		} catch (IOException ex) {
			throw new IllegalStateException("Unable to update trip.", ex);
		}
	}

	private ArrayNode readArray(ObjectMapper mapper, File file) throws IOException {
		JsonNode root = mapper.readTree(file);
		if (root != null && root.isArray()) {
			return (ArrayNode) root;
		}
		return mapper.createArrayNode();
	}

	private ObjectNode findNodeById(ArrayNode array, String field, String value) {
		for (JsonNode node : array) {
			if (node.has(field) && value.equals(node.get(field).asText())) {
				return (ObjectNode) node;
			}
		}
		return null;
	}

	private ObjectNode findNodeByName(ArrayNode array, String value) {
		for (JsonNode node : array) {
			if (node.has("name") && value.equals(node.get("name").asText())) {
				return (ObjectNode) node;
			}
		}
		return null;
	}

	private ObjectNode findSeatNode(ObjectNode transportNode, String seatsKey, String seatId) {
		if (transportNode == null || !transportNode.has("sections")) {
			return null;
		}
		for (JsonNode section : transportNode.get("sections")) {
			if (!section.has(seatsKey)) {
				continue;
			}
			for (JsonNode seat : section.get(seatsKey)) {
				if (seat.has("seatID") && seatId.equals(seat.get("seatID").asText())) {
					return (ObjectNode) seat;
				}
			}
		}
		return null;
	}

	private String seatsKey(JsonNode transportNode) {
		if (transportNode == null) {
			return "seats";
		}
		return "Boat".equals(transportNode.path("type").asText()) ? "cabins" : "seats";
	}

	private String expectedCompanyType(String tripType) {
		return switch (tripType) {
			case "Flight" -> "FlightCompany";
			case "CruiseLine" -> "BoatCompany";
			case "Route" -> "TrainCompany";
			default -> null;
		};
	}

	private String expectedTransportType(String tripType) {
		return switch (tripType) {
			case "Flight" -> "Plane";
			case "CruiseLine" -> "Boat";
			case "Route" -> "Train";
			default -> null;
		};
	}

	private void updateCompanyTrips(ArrayNode companies, String oldCompany, String newCompany, String tripId) {
		for (JsonNode companyNode : companies) {
			if (!companyNode.has("name")) {
				continue;
			}
			String companyName = companyNode.get("name").asText();
			if (oldCompany.equals(companyName) && companyNode.has("Trips")) {
				ArrayNode trips = (ArrayNode) companyNode.get("Trips");
				for (int i = trips.size() - 1; i >= 0; i--) {
					if (tripId.equals(trips.get(i).asText())) {
						trips.remove(i);
						break;
					}
				}
			}
			if (newCompany.equals(companyName)) {
				ArrayNode trips = companyNode.has("Trips") && companyNode.get("Trips").isArray()
						? (ArrayNode) companyNode.get("Trips")
						: new ObjectMapper().createArrayNode();
				if (!companyNode.has("Trips")) {
					((ObjectNode) companyNode).set("Trips", trips);
				}
				boolean alreadyPresent = false;
				for (JsonNode tripNode : trips) {
					if (tripId.equals(tripNode.asText())) {
						alreadyPresent = true;
						break;
					}
				}
				if (!alreadyPresent) {
					trips.add(tripId);
				}
			}
		}
	}
}