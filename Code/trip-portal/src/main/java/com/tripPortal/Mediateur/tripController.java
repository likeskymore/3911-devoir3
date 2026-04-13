package com.tripPortal.Mediateur;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPortal.Factory.CruiseLineFactory;
import com.tripPortal.Factory.FlightFactory;
import com.tripPortal.Factory.RouteFactory;
import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.BoatCompany;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.FlightCompany;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.TrainCompany;
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Visiteur.AllCruiseLines;
import com.tripPortal.Visiteur.AllFlights;
import com.tripPortal.Visiteur.AllRoutes;
import com.tripPortal.Visiteur.ListTripsDataStructure;

import javafx.util.Pair;




public class tripController {



	public Trip goCallCreateTrip(
            Company company,
            LocalDate startDate,
            LocalDate endDate,
            float price,
            int duration,
            ArrayList<Location> locations,
            Transport transport,
            String type) {

        if (company == null || locations == null || locations.isEmpty() || transport == null) {
            System.err.println("Invalid trip parameters.");
            return null;
        }

        Trip trip =  switch (type) {
            case "Flight"     -> FlightFactory.getInstance().createTrajectory(
                                    company, startDate, endDate, price, duration, locations, transport);
            case "CruiseLine" -> CruiseLineFactory.getInstance().createTrajectory(
                                    company, startDate, endDate, price, duration, locations, transport);
            case "Route"      -> RouteFactory.getInstance().createTrajectory(
                                    company, startDate, endDate, price, duration, locations, transport);
            default -> {
                System.err.println("Unknown trip type: " + type);
                yield null;
            }
        };

		
		return trip;
	}

	public Pair<ListTripsDataStructure, List<Trip>> fetchAllTripsAsStructure() {
		ListTripsDataStructure structure = new ListTripsDataStructure();
		List<Trip> loaded = new ArrayList<>(); // temp list for batching

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File("src/Database/Trip.json"));
			if (root == null || !root.isArray()) return new Pair<>(structure, loaded);

			for (JsonNode node : root) {
				if (!node.has("type")) continue;
				switch (node.get("type").asText()) {
					case "Flight" -> {
						AllFlights f = new AllFlights(node);
						structure.add(f);
					}
					case "Route" -> {
						AllRoutes r = new AllRoutes(node);
						structure.add(r);
					}
					case "CruiseLine" -> {
						AllCruiseLines c = new AllCruiseLines(node);
						structure.add(c);
					}
				}
			}

		} catch (IOException e) {
			System.err.println("Failed to read trips: " + e.getMessage());
		}

		return new Pair<>(structure, loaded);
	}

	public ArrayList<Trip> getAllTrips() {
		ArrayList<Trip> trips = new ArrayList<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File("src/Database/Trip.json"));
			if (root == null || !root.isArray()) return trips;

		for (JsonNode node : root) {
			Trip t = parseTripFromJsonNode(node);
			if (t != null) trips.add(t);
		}

		} catch (IOException e) {
			System.err.println("Failed to read trips: " + e.getMessage());
		}
		return trips;
	}

	private Trip parseTripFromJsonNode(JsonNode node) {
		if (!node.has("type")) return null;

		// ── common fields ──
		LocalDate startDate = LocalDate.parse(node.get("startDate").asText());
		LocalDate endDate   = LocalDate.parse(node.get("endDate").asText());
		float price         = (float) node.get("price").asDouble();
		int duration        = node.get("duration").asInt();

		// ── type-specific build ──
		Trip t = switch (node.get("type").asText()) {

			case "Flight" -> {
				Company company     = new FlightCompany(node.get("company").asText());
				Transport transport = new Plane(node.get("transport").asText());
				ArrayList<Location> locations = new ArrayList<>();
				locations.add(new Airport(node.get("origin").asText()));
				locations.add(new Airport(node.get("destination").asText()));

				yield FlightFactory.getInstance().createTrajectory(
					company, startDate, endDate, price, duration, locations, transport);
			}

			case "CruiseLine" -> {
				Company company     = new BoatCompany(node.get("company").asText());
				Transport transport = new Boat(node.get("transport").asText());
				ArrayList<Location> locations = new ArrayList<>();
				for (JsonNode port : node.get("path"))
					locations.add(new Port(port.asText()));

				yield CruiseLineFactory.getInstance().createTrajectory(
					company, startDate, endDate, price, duration, locations, transport);
			}

			case "Route" -> {
				Company company     = new TrainCompany(node.get("company").asText());
				Transport transport = new Train(node.get("transport").asText());
				ArrayList<Location> locations = new ArrayList<>();
				for (JsonNode city : node.get("path"))
					locations.add(new TrainStation(city.asText()));

				yield RouteFactory.getInstance().createTrajectory(
					company, startDate, endDate, price, duration, locations, transport);
			}

			default -> null;
		};

		if (t != null) return t;
		return null;
	}


	public void updateTripPrice(String tripId, float newPrice) {
    	List<Trip> trips = getAllTrips();

		for (Trip t : trips) {
			if (t.getId().equals(tripId)) {
				t.setPrice(newPrice);
				break;
			}
		}

			saveTripsToJson(trips); 
	}

	private void saveTripsToJson(List<Trip> trips) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/Database/Trip.json"), trips);
		} catch (IOException e) {
			System.err.println("Failed to save trips: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param trip
	 */
	public void deleteTrip(String tripId) {
		List<Trip> trips = getAllTrips();

		trips.removeIf(t -> t.getId().equals(tripId));

		saveTripsToJson(trips);

		// optionally update Company.json here too (NOT in UI)
	}
	
	/**
	 * 
	 * @param trip
	 * @param company
	 */
	public void editCompany(Trip trip, Company company) {
		// TODO - implement tripController.editCompany
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 * @param departureTime
	 */
	public void editDepartureTime(Trip trip, LocalTime departureTime) {
		// TODO - implement tripController.editDepartureTime
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 * @param price
	 */
	public void editPrice(Trip trip, float price) {
		// TODO - implement tripController.editPrice
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 * @param transport
	 */
	public void editTransport(Trip trip, Transport transport) {
		// TODO - implement tripController.editTransport
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param flight
	 * @param departure
	 */
	public void editDeparture(Flight flight, Airport departure) {
		// TODO - implement tripController.editDeparture
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param flight
	 * @param destionation
	 */
	public void editDestination(Flight flight, Airport destionation) {
		// TODO - implement tripController.editDestination
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 * @param newPath
	 */
	public void editTripRoute(Trip trip, ArrayList<Location> newPath) {
		// TODO - implement tripController.editTripRoute
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param departure
	 * @param destination
	 * @param departureDate
	 * @param section
	 */
	public ArrayList<Trip> searchTrips(Location departure, Location destination, LocalDate departureDate, SectionPlane section) {
		// TODO - implement tripController.searchTrips
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trip
	 */
	public boolean checkReservation(Trip trip) {
		// TODO - implement tripController.checkReservation
		throw new UnsupportedOperationException();
	}
	

}