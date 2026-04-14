package com.tripPortal.Etat;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class SeatStateController {

    private static final String STATE_FIELD = "state";
    private static final String AVAILABLE = "Available";
    private static final String RESERVED = "Reserved";
    private static final String OCCUPIED = "Occupied";


    public boolean reserveSeat(ObjectNode seatNode) {
        String currentState = getSeatState(seatNode);
        if (!AVAILABLE.equals(currentState)) {
            return false;
        }
        seatNode.put(STATE_FIELD, RESERVED);
        return true;
    }

    public boolean freeSeat(ObjectNode seatNode) {
        String currentState = getSeatState(seatNode);
        if (AVAILABLE.equals(currentState)) {
            return false;
        }
        seatNode.put(STATE_FIELD, AVAILABLE);
        return true;
    }

    public boolean occupySeat(ObjectNode seatNode) {
        String currentState = getSeatState(seatNode);
        if (!RESERVED.equals(currentState)) {
            return false;
        }
        seatNode.put(STATE_FIELD, OCCUPIED);
        return true;
    }

    public String getSeatState(ObjectNode seatNode) {
        if (seatNode == null) {
            return AVAILABLE;
        }
        return seatNode.path(STATE_FIELD).asText(AVAILABLE);
    }
}