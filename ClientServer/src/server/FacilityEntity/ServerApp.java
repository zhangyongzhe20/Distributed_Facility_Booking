package server.FacilityEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ServerApp {

    public static void main(String[] args) throws IOException, TimeoutException {
        Facility LT1 = new Facility("LT1");
        Facility LT2 = new Facility("LT2");
        Facility MR1 = new Facility("MR1");
        Facility MR2 = new Facility("MR2");

        ArrayList<Facility> facilityArrayList = new ArrayList<>();
        facilityArrayList.add(LT1);
        facilityArrayList.add(LT2);
        facilityArrayList.add(MR1);
        facilityArrayList.add(MR2);

        Server1_Boundary server1_boundary = new Server1_Boundary();
        server1_boundary.processRequest(facilityArrayList);
    }



}
