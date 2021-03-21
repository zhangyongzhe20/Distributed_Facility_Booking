package server.FacilityEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ServerApp {

    public static void main(String[] args) throws IOException, TimeoutException {
        Facility LT1 = new Facility("LT1", 1);
        Facility LT2 = new Facility("LT2", 2);
        Facility MR1 = new Facility("MR1", 3);
        Facility MR2 = new Facility("MR2", 4);

        ArrayList<Facility> facilityArrayList = new ArrayList<>();
        facilityArrayList.add(LT1);
        facilityArrayList.add(LT2);
        facilityArrayList.add(MR1);
        facilityArrayList.add(MR2);

        ArrayList<BookingID> BookingIDArrayList = new ArrayList<>();


        Server1_Boundary server1_boundary = new Server1_Boundary();
        Server2_Boundary server2_boundary = new Server2_Boundary();

        // server1_boundary.processRequest(facilityArrayList);
        server2_boundary.processRequest(facilityArrayList, BookingIDArrayList);

        for(BookingID b: BookingIDArrayList)
        {
            System.out.println("server side: " + b.getBookingInfoString());
        }

    }



}
