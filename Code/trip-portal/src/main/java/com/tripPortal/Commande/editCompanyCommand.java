
package com.tripPortal.Commande;
import java.util.ArrayList;
import java.util.Scanner;

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

	public void execute() {
		company.updateName(newName, oldName);
	}

	public void undo() {
		// TODO - implement editCompanyCommand.undo
		throw new UnsupportedOperationException();
	}

	public companyMemento establish(){
		companyMemento m = new companyMemento();
		
		return m;
	}

}