import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.Trip;

import java.util.ArrayList;

public abstract class PlaneTripFactory extends TripFactory {

	/**
	 * 
	 * @param city
	 */
	public Location createLocation(String city) {
		// TODO - implement PlaneTripFactory.createLocation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	public Company createCompany(String name) {
		// TODO - implement PlaneTripFactory.createCompany
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param locations
	 */
	public Trip createTrajectory(ArrayList<Location> locations) {
		// TODO - implement PlaneTripFactory.createTrajectory
		throw new UnsupportedOperationException();
	}

}