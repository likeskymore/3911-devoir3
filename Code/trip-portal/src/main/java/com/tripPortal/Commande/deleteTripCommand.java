package com.tripPortal.Commande;

import java.io.IOException;

import com.tripPortal.Model.Trip;

public class deleteTripCommand implements Command{
    
    Trip trip;
    public deleteTripCommand (Trip trip){
        this.trip = trip;
    }

    public void execute(){
        try {
            trip.delete();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void undo(){

    }
}
