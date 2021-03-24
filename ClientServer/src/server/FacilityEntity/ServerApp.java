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
        Facility MR1 = new Facility("MR1", 3);
        Facility MR2 = new Facility("MR2", 4);

        ArrayList<Facility> facilityArrayList = new ArrayList<>();
        facilityArrayList.add(LT1);
        facilityArrayList.add(LT2);
        facilityArrayList.add(MR1);
        facilityArrayList.add(MR2);


        ArrayList<BookingID> BookingIDArrayList = new ArrayList<>();


        // Add some user predefined data here
        LT1.bookAvailability(5,7); // 2021-03-29 14-15
        LT1.bookAvailability(5,8); // 2021-03-29 15-16
        LT2.bookAvailability(3, 2); // 2021-03-27 9-10

        BookingID testBookID1 = new BookingID(1,5,1,14, 16);
        BookingID testBookID2 = new BookingID(2,3,2,9,10);
        BookingIDArrayList.add(testBookID1);
        BookingIDArrayList.add(testBookID2);

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

                for (BookingID id: BookingIDArrayList){
                    System.out.println("[Server APP]    Booking ID is: "+ id.getID());
                    System.out.println("[Server APP]    The Booking info is: "+ id.getBookingInfoString());
                }
            } else if (control.getServiceID_receive() == 3){
                control.clearDataToBeUnMarshal();
                server3_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
            }
        }
        // server3_boundary.processRequest(facilityArrayList, BookingIDArrayList);

    }



}
