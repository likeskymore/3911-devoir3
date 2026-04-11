package com.tripPortal.Mediateur;

//import com.sun.jdi.connect.Transport;
import com.sun.jdi.connect.Transport;
import com.tripPortal.Factory.BoatCompanyFactory;
import com.tripPortal.Factory.FlightCompanyFactory;
import com.tripPortal.Factory.TrainCompanyFactory;
import com.tripPortal.Model.Company;
import com.tripPortal.Model.Trip;
import java.util.ArrayList;

public class companyController {

	public void goCallCreateCompany(String name, String type) {
		if (name == null || name.isEmpty()) {
			System.err.println("Company name cannot be empty.");
			return;
		}

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

	/**
	 * 
	 * @param name
	 */
	public void updateCompanyName(String name) {
		// TODO - implement companyController.updateCompanyName
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param vehicules
	 */
	public void updateCompanyVehicules(ArrayList<Transport> vehicules) {
		// TODO - implement companyController.updateCompanyVehicules
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param trips
	 */
	public void updateCompanyTrips(ArrayList<Trip> trips) {
		// TODO - implement companyController.updateCompanyTrips
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param company
	 */
	public void deleteCompany(Company company) {
		// TODO - implement companyController.deleteCompany
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param company
	 */
	public void deleteCompanyTransport(Company company) {
		// TODO - implement companyController.deleteCompanyTransport
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param company
	 */
	public void deleteCompanyTrips(Company company) {
		// TODO - implement companyController.deleteCompanyTrips
		throw new UnsupportedOperationException();
	}

}