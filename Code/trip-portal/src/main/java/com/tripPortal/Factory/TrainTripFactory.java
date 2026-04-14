package com.tripPortal.Factory;

import com.tripPortal.Model.Company;
import com.tripPortal.Model.Location;
import com.tripPortal.Model.TransportPrototype;
import com.tripPortal.Model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class TrainTripFactory extends TripFactory {

	public abstract Location createLocation(String city);
    public abstract Company createCompany(String name);
    public abstract Trip createTrajectory(
        Company company,
        LocalDate startDate,
        LocalDate endDate,
        float price,
        int duration,
        ArrayList<Location> locations,
        TransportPrototype transport
    );
}