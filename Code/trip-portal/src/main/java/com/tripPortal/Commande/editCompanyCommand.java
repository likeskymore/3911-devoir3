
package com.tripPortal.Commande;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tripPortal.Model.Company;

public class editCompanyCommand implements Command {

	Company company;
	String newName;
	String oldName;

	public editCompanyCommand(Company company, String newName, String oldName){
		this.company = company;
		this.newName = newName;
		this.oldName = oldName;
	}

	public editCompanyCommand(){
		// this is for undo
	}

	public void execute() {
		company.updateName(newName, oldName);
	}

	public void undo() {
		try {
			ObjectMapper CompanyAndTripsToRestoreMapper = new ObjectMapper();
			File CompanyAndTripsToRestoreFile = new File("src/Database/companyUpdateNameHistory.json");
			JsonNode  CompanyAndTripsToRestore = CompanyAndTripsToRestoreMapper.readTree(CompanyAndTripsToRestoreFile);

			JsonNode newName = CompanyAndTripsToRestore.get("newCompanyName");
			JsonNode oldName = CompanyAndTripsToRestore.get("oldCompanyName");
			JsonNode tripsToRestore = CompanyAndTripsToRestore.get("tripsToUpdate");

			ObjectMapper companyMapper = new ObjectMapper();
			File companyFile = new File("src/Database/Company.json");
			ArrayNode companies = (ArrayNode) companyMapper.readTree(companyFile);

			for (int i = 0; i < companies.size(); i++){
				if(companies.get(i).get("name").asText().equals(newName.asText())){
					((ObjectNode) companies.get(i)).set("name", oldName);
				}
			}

			ObjectMapper tripMapper = new ObjectMapper();
			File tripFile = new File("src/Database/Trip.json");
			ArrayNode trips = (ArrayNode) tripMapper.readTree(tripFile);
			for (int i = 0; i < tripsToRestore.size(); i++){
				for(int j = 0; j < trips.size(); j++){
					if (trips.get(j).get("id").asText().equals(tripsToRestore.get(i).asText())){
						((ObjectNode) trips.get(j)).set("company", oldName);
					}
				}
			}
			companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
			tripMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
			CompanyAndTripsToRestoreMapper.writerWithDefaultPrettyPrinter().writeValue(CompanyAndTripsToRestoreFile, 
				CompanyAndTripsToRestoreMapper.createObjectNode()
			);

		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}

	public companyMemento establish(){
		companyMemento m = new companyMemento();
		
		return m;
	}

}