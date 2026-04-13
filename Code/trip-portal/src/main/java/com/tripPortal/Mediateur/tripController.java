package com.tripPortal.Mediateur;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPortal.Commande.Command;
import com.tripPortal.Factory.CruiseLineFactory;
import com.tripPortal.Factory.FlightFactory;
import com.tripPortal.Factory.RouteFactory;
import com.tripPortal.Model.Airport;
import com.tripPortal.Model.Boat;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.CruiseLine;
import com.tripPortal.Model.Flight;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Plane;
import com.tripPortal.Model.Port;
import com.tripPortal.Model.Route;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.Train;
import com.tripPortal.Model.TrainStation;
import com.tripPortal.Model.Transport;
import com.tripPortal.Model.Trip;
import com.tripPortal.Visiteur.AllCruiseLines;
import com.tripPortal.Visiteur.AllFlights;
import com.tripPortal.Visiteur.AllRoutes;
import com.tripPortal.Visiteur.ListTripsDataStructure;


public class tripController {

	Command command;

	public void setCommand(Command command){
		this.command = command;
	}

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

        return switch (type) {
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
    }

	public ListTripsDataStructure fetchAllTripsAsStructure() {
		ListTripsDataStructure structure = new ListTripsDataStructure();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File("src/Database/Trip.json"));
			if (root == null || !root.isArray()) return structure;

			for (JsonNode node : root) {
				if (!node.has("type")) continue;
				switch (node.get("type").asText()) {
					case "Flight"     -> structure.add(new AllFlights(node));
					case "Route"      -> structure.add(new AllRoutes(node));
					case "CruiseLine" -> structure.add(new AllCruiseLines(node));
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to read trips: " + e.getMessage());
		}
		return structure;
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

	// /**
	//  * 
	//  * @param trip
	//  */
	// public void deleteTrip(Trip trip) {
	// 	// TODO - implement tripController.deleteTrip
	// 	throw new UnsupportedOperationException();
	// }

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

	public void deleteTrip(){
		command.execute();
	}

	public void updatePrice(){
		command.execute();
	}

}