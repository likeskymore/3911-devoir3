package com.tripPortal.Commande;

import com.tripPortal.Model.Location;

public class deleteLocationCommand implements Command {
    Location location;

    public deleteLocationCommand(Location location){
        this.location = location;
    }

    @Override
    public void execute() {
        location.delete();
    }
    @Override
    public void undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
}
