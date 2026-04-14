package com.tripPortal.Mediateur;

//import com.sun.jdi.connect.Transport;
import com.sun.jdi.connect.Transport;
import com.tripPortal.Commande.Command;
import com.tripPortal.Factory.AirportFactory;
import com.tripPortal.Factory.BoatCompanyFactory;
import com.tripPortal.Factory.FlightCompanyFactory;
import com.tripPortal.Factory.TrainCompanyFactory;
import com.tripPortal.Factory.TrainStationFactory;
import com.tripPortal.Factory.PortFactory;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Trip;
import java.util.ArrayList;

public class companyController {
	Command command;

	public void setCommand(Command command){
		this.command = command;
	}

	public void goCallCreateCompany(String name, String type) {

		if (type == "FlightCompany") {
			FlightCompanyFactory.getInstance().createCompany(name);
		} else if (type == "CruiseCompany") {
			BoatCompanyFactory.getInstance().createCompany(name);
		} else if (type == "TrainCompany") {
			TrainCompanyFactory.getInstance().createCompany(name);
		} else {
			System.err.println("Unknown company type: " + type);
		}
		
	}

	public void deleteCompany(){
		command.execute();
	}

	public void updateCompanyName(){
		command.execute();
	}

	public void undoDeleteCompany(){
		command.undo();
	}

	public void undoUpdateCompanyName(){
		command.undo();
	}

}