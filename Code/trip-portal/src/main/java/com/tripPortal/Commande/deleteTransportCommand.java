package com.tripPortal.Commande;

import com.tripPortal.Model.Transport;

public class deleteTransportCommand implements Command{

    Transport transport;
    public deleteTransportCommand(Transport transport){
        this.transport = transport;
    }
    @Override
    public void execute() {
        transport.delete(transport.getTransportID());
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
    
    
}
