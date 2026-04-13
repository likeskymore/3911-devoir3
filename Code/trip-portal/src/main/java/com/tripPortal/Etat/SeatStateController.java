package com.tripPortal.Etat;

import com.tripPortal.Model.Seat;

public class SeatStateController {

    public void reserveSeat(Seat s) {
        if (!(s.getCurrentState() instanceof Available)) {
            System.out.println("Seat " + s.getSeatID() + " cannot be reserved — currently " 
                + s.getCurrentState().getClass().getSimpleName());
            return;
        }
        s.next(); 
    }

    public void freeSeat(Seat s) {
        if (s.getCurrentState() instanceof Available) {
            System.out.println("Seat " + s.getSeatID() + " is already free.");
            return;
        }
        s.cancel(); 
    }

    public void occupySeat(Seat s) {
        if (!(s.getCurrentState() instanceof Reserved)) {
            System.out.println("Seat " + s.getSeatID() + " cannot be occupied — currently "
                + s.getCurrentState().getClass().getSimpleName());
            return;
        }
        s.next(); 
    }

}