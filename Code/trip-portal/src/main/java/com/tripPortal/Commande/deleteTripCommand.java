package com.tripPortal.Commande;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tripPortal.Model.Trip;

public class deleteTripCommand implements Command{
    
    Trip trip;
    public deleteTripCommand (Trip trip){
        this.trip = trip;
    }

    public deleteTripCommand(){

    }

    public void execute(){
        try {
            trip.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undo(){
        ObjectMapper historyMapper = new ObjectMapper();
        File historyFile = new File("src/Database/tripDeleteHistory.json");
        JsonNode deletedTrip = null;
        try {
            deletedTrip = historyMapper.readTree(historyFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ObjectMapper tripMapper = new ObjectMapper();
        File tripFile = new File("src/Database/Trip.json");
        ArrayNode trips = null;
        try {
            trips = (ArrayNode) tripMapper.readTree(tripFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        trips.add(deletedTrip);

        ObjectMapper companyMapper = new ObjectMapper();
        File companyFile = new File("src/Database/Company.json");
        ArrayNode companies = null;
        try {
            companies = (ArrayNode) companyMapper.readTree(companyFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < companies.size(); i++){
            if (companies.get(i).get("name").asText().equals(deletedTrip.get("company").asText())){
                ArrayNode companyNode = (ArrayNode) companies.get(i).get("Trips");
                companyNode.add(deletedTrip.get("id").asText());
                break;
            }
        }

        try {
            tripMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
            companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
            historyMapper.writerWithDefaultPrettyPrinter().writeValue(historyFile, historyMapper.createObjectNode());
        } catch (IOException e) {
            e.printStackTrace();
}
    }
}
