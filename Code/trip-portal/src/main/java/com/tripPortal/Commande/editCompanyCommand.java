
package com.tripPortal.Commande;
import java.util.ArrayList;
import java.util.Scanner;

import com.tripPortal.Model.Company;

public class editCompanyCommand implements Command {

	Company company;

	// code fait avec l'aide de Claudeadf
	public editCompanyCommand(Company company){
		this.company = company;
	}

	public void execute() {
		// TODO - implement editCompanyCommand.execute
		Scanner scanner = new Scanner(System.in);
		System.out.println("New name for Company: ");
		String input = scanner.nextLine();
		company.setName(input);
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