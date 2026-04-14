package com.tripPortal.Commande;

import java.io.File;
import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.TransportPrototype;

public class deleteTransportCommand implements Command{

    TransportPrototype transport;
    public deleteTransportCommand(TransportPrototype transport){
        this.transport = transport;
    }

    public deleteTransportCommand(){
        //this one is for undo
    }
    @Override
    public void execute() {
        transport.delete(transport.getTransportID());
    }

    @Override
    public void undo() {
        try {
            ObjectMapper transportToRestoreMapper = new ObjectMapper();
            File transportToRestoreFile = new File("src/Database/transportDeleteHistory.json");
            JsonNode transportToRestore = transportToRestoreMapper.readTree(transportToRestoreFile);

            JsonNode transport = transportToRestore.get("transport");
            ArrayNode tripsRemoved = (ArrayNode) transportToRestore.get("tripsRemoved");
            ArrayNode tripsFromCompany = (ArrayNode) transportToRestore.get("tripsFromCompany");

            ObjectMapper transportMapper = new ObjectMapper();
            File transportFile = new File("src/Database/Transport.json");
            ArrayNode Transports = (ArrayNode) transportMapper.readTree(transportFile);

            Transports.add(transport);

            ObjectMapper tripsMapper = new ObjectMapper();
            File tripsFile = new File("src/Database/Trip.json");
            ArrayNode trips = (ArrayNode) tripsMapper.readTree(tripsFile);

            for (int i = 0; i < tripsRemoved.size(); i++){
                trips.add(tripsRemoved.get(i));
            }

            ObjectMapper companyMapper = new ObjectMapper();
            File companyFile = new File("src/Database/Company.json");
            ArrayNode companies = (ArrayNode) companyMapper.readTree(companyFile);

            for (int i = 0; i < tripsRemoved.size(); i ++){
                for (int j = 0; j < companies.size(); j++){
                    if(companies.get(j).get("name").asText().equals
                    (tripsRemoved.get(i).get("company").asText())){
                       ((ArrayNode) companies.get(j).get("Trips")).add(tripsRemoved.get(i).get("id")); 
                    }
                }
            }

            transportMapper.writerWithDefaultPrettyPrinter().writeValue(transportFile, Transports);
            tripsMapper.writerWithDefaultPrettyPrinter().writeValue(tripsFile, trips);
            companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
            transportToRestoreMapper.writerWithDefaultPrettyPrinter().writeValue(transportToRestoreFile, transportToRestoreMapper.createObjectNode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
}
