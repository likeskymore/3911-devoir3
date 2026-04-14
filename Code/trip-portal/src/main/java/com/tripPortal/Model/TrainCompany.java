package com.tripPortal.Model;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class TrainCompany extends Company {

	public TrainCompany(String name) {
		super(name);
	}
  
  public TrainCompany(){
        super();
	}

    public TrainCompany(JsonNode node) {
        super(node.get("name").asText());
        setId(node.get("id").asText());
        setTripID(node.get("tripId").asText());
    }

}