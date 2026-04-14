package com.tripPortal.Mediateur;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Commande.Command;
import com.tripPortal.Factory.AirportFactory;
import com.tripPortal.Factory.PortFactory;
import com.tripPortal.Factory.TrainStationFactory;
import com.tripPortal.Model.Location;

public class locationController {
	
	Command command;

	public void setCommand(Command command){
		this.command = command;
	}

	public void goCallCreateLocation(String city, String name, String type) {

		if ("Airport".equals(type)) {
			AirportFactory.getInstance().createLocation(city, name);
		} else if ("Port".equals(type)) {
			PortFactory.getInstance().createLocation(city, name);
		} else if ("Station".equals(type)) {
			TrainStationFactory.getInstance().createLocation(city, name);
		} else {
			System.err.println("Unknown location type: " + type);
		}
		
	}

	public void goCallCreateLocation(String city, String type) {
		goCallCreateLocation(city, city, type);
	}

	public void updateLocation(){
		command.execute();
	}

	public void deleteLocation(){
		command.execute();
	}

	public void undoUpdateLocation(){
		command.undo();
	}

	public void undoDeleteLocation(){
		command.undo();
	}
}