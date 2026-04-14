package com.tripPortal.Commande;

import java.io.File;
import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
        try {
            ObjectMapper locationRestorerMapper = new ObjectMapper();
            File locationRestorerFile = new File("src/Database/locationDeleteHistory.json");
            JsonNode locationToRestore = locationRestorerMapper.readTree(locationRestorerFile);

            JsonNode location = locationToRestore.get("location");
            ArrayNode tripsRemoved = (ArrayNode) locationToRestore.get("tripsRemoved");
            ArrayNode tripsFromCompany = (ArrayNode) locationToRestore.get("tripsFromCompany");
            
            ObjectMapper locationMapper = new ObjectMapper();
            File locationFile = new File("src/Database/Location.json");
            ArrayNode locations = (ArrayNode) locationMapper.readTree(locationFile);

            locations.add(location);

            locationMapper.writerWithDefaultPrettyPrinter().writeValue(locationFile, locations);
            locationRestorerMapper.writerWithDefaultPrettyPrinter().writeValue(locationRestorerFile,
                locationRestorerMapper.createObjectNode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}
