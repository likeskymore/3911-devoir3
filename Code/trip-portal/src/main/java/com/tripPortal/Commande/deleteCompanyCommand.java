package com.tripPortal.Commande;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tripPortal.Model.Company;

public class deleteCompanyCommand implements Command {

    Company company;
    
    public deleteCompanyCommand(Company company){
        this.company = company;
    }

    public deleteCompanyCommand(){
        //this is for undo
    }
    
    public void execute() {
        company.deleteCompany();
    }

    
    public void undo() {
        try {
            ObjectMapper companyMapper = new ObjectMapper();
            File companyFile = new File("src/Database/Company.json");
            ArrayNode companies = (ArrayNode) companyMapper.readTree(companyFile);

            ObjectMapper restoreMapper = new ObjectMapper();
            File restoreFile = new File("src/Database/companyDeleteHistory.json");
            JsonNode companyAndTripToRestore = restoreMapper.readTree(restoreFile);

            JsonNode companyToRestore = companyAndTripToRestore.get("company");
            JsonNode tripToRestore = companyAndTripToRestore.get("trips");

            ObjectMapper tripMapper = new ObjectMapper();
            File tripFile = new File("src/Database/Trip.json");
            ArrayNode trips = (ArrayNode) tripMapper.readTree(tripFile);

            companies.add(companyToRestore);
            for (int i = 0; i < tripToRestore.size(); i++){
                trips.add(tripToRestore.get(i));
            }

            companyMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, companies);
            tripMapper.writerWithDefaultPrettyPrinter().writeValue(tripFile, trips);
            restoreMapper.writerWithDefaultPrettyPrinter().writeValue(restoreFile, restoreMapper.createObjectNode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
