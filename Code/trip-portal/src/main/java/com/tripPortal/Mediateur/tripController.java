package com.tripPortal.Mediateur;

import com.tripPortal.Factory.CruiseLineFactory;
import com.tripPortal.Factory.FlightFactory;
import com.tripPortal.Factory.RouteFactory;
import com.tripPortal.Model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


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
	 * @param trip
	 */
	public void deleteTrip(Trip trip) {
		// TODO - implement tripController.deleteTrip
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