package client.boundary;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Boundary_Factory {
    List<Boundary> allBoundaries;

    public Boundary_Factory() throws SocketException, UnknownHostException {
        allBoundaries = new ArrayList<>();
        allBoundaries.add(new Service1_Boundary());
        allBoundaries.add(new Service2_Boundary());
        allBoundaries.add(new Service3_Boundary());
        allBoundaries.add(new Service4_Boundary());
        allBoundaries.add(new Service5_Boundary());
        allBoundaries.add(new Service6_Boundary());
    }
    public Boundary createBoundary(int selection) { return allBoundaries.get(selection - 1);}

    public int getNumOfBoundaries(){ return allBoundaries.size();}

}
