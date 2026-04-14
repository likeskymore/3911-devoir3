package com.tripPortal.Model;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Reservation {

	private String reservationNumber;
	private LocalTime startTimeReservation;
	private String transportID;
	private String reservedSeat;
	private String tripId;
	private boolean isPaid;

	public Reservation(String reservationNumber, String transportID, String reservedSeat, String tripId,
			boolean isPaid) {
		if (reservationNumber == null || reservationNumber.isEmpty()) {
			this.reservationNumber = generateReservationNumber();
		} else {
			this.reservationNumber = reservationNumber;
		}
		this.startTimeReservation = LocalTime.now();
		this.transportID = transportID;
		this.reservedSeat = reservedSeat;
		this.tripId = tripId;
		this.isPaid = isPaid;
	}

	public String generateReservationNumber() {
		String id = "";
		Random rand = new Random();
		boolean isUnique = false;
		
		while (!isUnique) {
			id = "";
			for (int i = 0; i < 6; i++) {
				int number = (rand.nextInt(9));
				id += number;
			}
			
			isUnique = isReservationNumberUnique(id);
		}
		return id;
	}
	
	private boolean isReservationNumberUnique(String reservationNumber) {
    try {
        var mapper = new ObjectMapper();
        var file = new File("src/Database/User.json");
        if (!file.exists()) return true;

        JsonNode root = mapper.readTree(file);

        ArrayNode users;
        if (root.isArray()) {
            users = (ArrayNode) root;
        } else if (root.has("users") && root.get("users").isArray()) {
            users = (ArrayNode) root.get("users");
        } else {
            return true;
        }

        for (var user : users) {
            if (user.has("reservations") && user.get("reservations").isArray()) {
                for (var reservation : user.get("reservations")) {
                    if (reservation.has("reservationNumber") &&
                        reservation.get("reservationNumber").asText().equals(reservationNumber)) {
                        return false;
                    }
                }
            }
        }
        return true;
    } catch (IOException ex) {
        return true;
    }
}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public LocalTime getStartTimeReservation() {
		return startTimeReservation;
	}

	public String getTransportID() {
		return transportID;
	}

	public String getReservedSeat() {
		return reservedSeat;
	}

	public String getTripId() {
		return tripId;
	}

	public boolean isPaid() {
		return isPaid;
	}

}