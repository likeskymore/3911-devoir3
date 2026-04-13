package com.tripPortal.Commande;

import com.tripPortal.Model.Location;

public class editLocationCommand implements Command{


    Location location;
    String newName;
    String newCity;

    public editLocationCommand(Location location, String newName, String newCity){
        this.location = location;
        this.newCity = newCity;
        this.newName = newName;
    }


    public void execute() {
        location.update(newName, newCity);
    }


    public void undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
    
}