package com.tripPortal.Commande;

import com.tripPortal.Model.Location;

public class deleteLocationCommand implements Command {
    Location location;

    public deleteLocationCommand(Location location){
        this.location = location;
    }
    public deleteLocationCommand(){
        //this one is for undo
    }

    @Override
    public void execute() {
        location.delete();
    }
    @Override
    public void undo() {
        
    }
}
