package com.tripPortal.Commande;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public editLocationCommand(Location location){
        this.location = location;
        //this one is for undo
    }

    public void execute() {
        location.update(newName, newCity);
    }


    public void undo() {
        try {
            ObjectMapper locationRestorerMapper = new ObjectMapper();
            File locationRestorerFile = new File("src/Database/locationUpdateHistory.json");
            JsonNode LocationToRestore = locationRestorerMapper.readTree(locationRestorerFile);

            JsonNode cityToRestore = LocationToRestore.get("city");
            JsonNode nameToRestore = LocationToRestore.get("name");
            String locationId = LocationToRestore.get("id").asText();

            ObjectMapper locationMapper = new ObjectMapper();
            File locationFile = new File("src/Database/Location.json");
            ArrayNode locations = (ArrayNode) locationMapper.readTree(locationFile);

            for (int i = 0; i < locations.size(); i++){
                if (locations.get(i).get("id").asText().equals(locationId)){
                   ((ObjectNode) locations.get(i)).set("city", cityToRestore);
                   ((ObjectNode) locations.get(i)).set("name", nameToRestore);
                }
            }

            locationMapper.writerWithDefaultPrettyPrinter().writeValue(locationFile, locations);
            locationRestorerMapper.writerWithDefaultPrettyPrinter().writeValue(locationRestorerFile, locationRestorerMapper.createObjectNode());



        } catch (Exception e) {
            e.printStackTrace();
        }


        

    }
    
}