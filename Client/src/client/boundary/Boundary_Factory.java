package client.boundary;

import java.util.ArrayList;
import java.util.List;

public class Boundary_Factory {
    List<Boundary> allBoundaries;

    public Boundary_Factory() {
        allBoundaries = new ArrayList<>();
        allBoundaries.add(new Service1_Boundary());
        allBoundaries.add(new Service2_Boundary());
        allBoundaries.add(new Service3_Boundary());
        allBoundaries.add(new Service4_Boundary());
        allBoundaries.add(new Service5_Boundary());
    }
    public Boundary createBoundary(String selection) { return allBoundaries.get(Integer.parseInt(selection) - 1);}

}
