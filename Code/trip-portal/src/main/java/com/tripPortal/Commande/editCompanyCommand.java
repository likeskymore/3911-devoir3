
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
			if (CompanyAndTripsToRestore == null || !CompanyAndTripsToRestore.isObject()) {
				throw new IllegalStateException("No company update to undo.");
			}

			JsonNode newName = CompanyAndTripsToRestore.get("newCompanyName");
			JsonNode oldName = CompanyAndTripsToRestore.get("oldCompanyName");
			JsonNode tripsToRestore = CompanyAndTripsToRestore.get("tripsToUpdate");
			if (newName == null || oldName == null || newName.asText("").isBlank() || oldName.asText("").isBlank()) {
				throw new IllegalStateException("No company update to undo.");
			}

			ObjectMapper companyMapper = new ObjectMapper();
			File companyFile = new File("src/Database/Company.json");
			JsonNode companyRoot = companyMapper.readTree(companyFile);
			ArrayNode companies = companyRoot != null && companyRoot.isArray()
					? (ArrayNode) companyRoot
					: companyMapper.createArrayNode();

			boolean companyFound = false;
			for (int i = 0; i < companies.size(); i++){
				if(companies.get(i).get("name").asText().equals(newName.asText())){
					companyFound = true;
					((ObjectNode) companies.get(i)).set("name", oldName);
					break;
				}
			}
			if (!companyFound) {
				throw new IllegalStateException("Cannot undo company update because the company no longer exists. Undo delete first.");
			}

			ObjectMapper tripMapper = new ObjectMapper();
			File tripFile = new File("src/Database/Trip.json");
			JsonNode tripRoot = tripMapper.readTree(tripFile);
			ArrayNode trips = tripRoot != null && tripRoot.isArray()
					? (ArrayNode) tripRoot
					: tripMapper.createArrayNode();
			ArrayNode tripsToRestoreArray = tripsToRestore != null && tripsToRestore.isArray()
					? (ArrayNode) tripsToRestore
					: tripMapper.createArrayNode();
			for (int i = 0; i < tripsToRestoreArray.size(); i++){
				for(int j = 0; j < trips.size(); j++){
					if (trips.get(j).get("id").asText().equals(tripsToRestoreArray.get(i).asText())){
						((ObjectNode) trips.get(j)).set("company", oldName);
					}
				}
			}
			companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
			tripMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
			CompanyAndTripsToRestoreMapper.writerWithDefaultPrettyPrinter().writeValue(CompanyAndTripsToRestoreFile, 
				CompanyAndTripsToRestoreMapper.createObjectNode()
			);

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to undo company update.", e);
		}
		 
	}

}