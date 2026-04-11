package com.tripPortal.Mediateur;

import java.util.List;

import com.tripPortal.Factory.BoatFactory;
import com.tripPortal.Factory.PlaneFactory;
import com.tripPortal.Factory.TrainFactory;
import com.tripPortal.Model.Section;
import com.tripPortal.Model.SectionBoat;
import com.tripPortal.Model.SectionPlane;
import com.tripPortal.Model.SectionTrain;
import com.tripPortal.Model.Transport;

public class transportController {

    public Transport goCallCreateTransport(String name, String type, List<? extends Section> sections) {
        if (name == null || name.isEmpty()) {
            System.err.println("Transport name cannot be empty.");
            return null;
        }

        if (type.equals("Plane")) {
            return PlaneFactory.getInstance().createTransport(name, (List<SectionPlane>) sections);
        } else if (type.equals("Boat")) {
            return BoatFactory.getInstance().createTransport(name, (List<SectionBoat>) sections);
        } else if (type.equals("Train")) {
            return TrainFactory.getInstance().createTransport(name, (List<SectionTrain>) sections);
        } else {
            System.err.println("Unknown transport type: " + type);
        }
        return null;
    }
}