package com.tripPortal.Commande;

import com.tripPortal.Model.Company;

public class deleteCompanyCommand implements Command {

    Company company;
    
    public deleteCompanyCommand(Company company){
        this.company = company;
    }
    
    public void execute() {
        company.deleteCompany();
    }

    
    public void undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
    
}
