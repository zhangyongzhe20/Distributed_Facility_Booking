package server.FacilityEntity;

import server.control.Control;
import utils.UnMarshal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ServerApp {

    public static void main(String[] args) throws IOException, TimeoutException {
        Facility LT1 = new Facility("LT1", 1);
        Facility LT2 = new Facility("LT2", 2);
        LT2.bookAvailability(3, 2);
        Facility MR1 = new Facility("MR1", 3);
        Facility MR2 = new Facility("MR2", 4);

        ArrayList<Facility> facilityArrayList = new ArrayList<>();
        facilityArrayList.add(LT1);
        facilityArrayList.add(LT2);
        facilityArrayList.add(MR1);
        facilityArrayList.add(MR2);


        ArrayList<BookingID> BookingIDArrayList = new ArrayList<>();


        Control control = new Control();
        Server1_Boundary server1_boundary = new Server1_Boundary();
        Server2_Boundary server2_boundary = new Server2_Boundary();
        Server3_Boundary server3_boundary = new Server3_Boundary();

        while (true)
        {
            byte[] dataTobeUnmarshal = control.receive();
            if (control.getServiceID_receive() == 1){
                control.clearDataToBeUnMarshal();
                server1_boundary.processRequest(dataTobeUnmarshal, facilityArrayList);
                server1_boundary.clearQueryInfo();
            }
            else if (control.getServiceID_receive() ==  2){
                control.clearDataToBeUnMarshal();
                server2_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
                server2_boundary.clearTimeSlotInfo();
            }
        }
        // server3_boundary.processRequest(facilityArrayList, BookingIDArrayList);

    }



}
