import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Trip;

import java.util.ArrayList;

public abstract class TrainTripFactory extends TripFactory {

	/**
	 * 
	 * @param city
	 */
	public Location createLocation(String city) {
		// TODO - implement TrainTripFactory.createLocation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	public Company createCompany(String name) {
		// TODO - implement TrainTripFactory.createCompany
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param locations
	 */
	public Trip createTrajectory(ArrayList<Location> locations) {
		// TODO - implement TrainTripFactory.createTrajectory
		throw new UnsupportedOperationException();
	}

}