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
    }

    public editLocationCommand(){
    }

    public void execute() {
        location.update(newName, newCity);
    }


    public void undo() {
        try {
            ObjectMapper locationRestorerMapper = new ObjectMapper();
            File locationRestorerFile = new File("src/Database/locationUpdateHistory.json");
            JsonNode LocationToRestore = locationRestorerMapper.readTree(locationRestorerFile);

            if (LocationToRestore == null || !LocationToRestore.isObject()) {
                throw new IllegalStateException("No location update to undo.");
            }

            JsonNode cityToRestore = LocationToRestore.get("city");
            JsonNode nameToRestore = LocationToRestore.get("name");
            String locationId = LocationToRestore.get("id").asText();
            if (locationId.isBlank() || cityToRestore == null || nameToRestore == null) {
                throw new IllegalStateException("No location update to undo.");
            }

            ObjectMapper locationMapper = new ObjectMapper();
            File locationFile = new File("src/Database/Location.json");
            JsonNode locationRoot = locationMapper.readTree(locationFile);
            ArrayNode locations = locationRoot != null && locationRoot.isArray()
                    ? (ArrayNode) locationRoot
                    : locationMapper.createArrayNode();

            boolean locationFound = false;
            for (int i = 0; i < locations.size(); i++){
                if (locations.get(i).get("id").asText().equals(locationId)){
                   locationFound = true;
                   ((ObjectNode) locations.get(i)).set("city", cityToRestore);
                   ((ObjectNode) locations.get(i)).set("name", nameToRestore);
                   break;
                }
            }

            if (!locationFound) {
                throw new IllegalStateException("Cannot undo location update because the location no longer exists. Undo delete first.");
            }

            locationMapper.writerWithDefaultPrettyPrinter().writeValue(locationFile, locations);
            locationRestorerMapper.writerWithDefaultPrettyPrinter().writeValue(locationRestorerFile, locationRestorerMapper.createObjectNode());



        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to undo location update.", e);
        }


        

    }
    
}