package com.tripPortal.Commande;

import com.tripPortal.Model.TransportPrototype;

public class deleteTransportCommand implements Command{

    TransportPrototype transport;
    public deleteTransportCommand(TransportPrototype transport){
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
