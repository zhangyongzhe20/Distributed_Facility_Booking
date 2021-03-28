package server.FacilityEntity;

import server.control.Control;
import server.control.UDPserver;
import utils.UnMarshal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static client.config.Constants.*;
import static utils.Marshal.marshalMsgData;

public class ServerApp {
    public static HashMap<Integer, byte[]> ProcessedTable = new HashMap<>();

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
        LT1.bookAvailability(1,2); // 2021-03-30 14-15
        LT1.bookAvailability(1,3); // 2021-03-30 15-16
        LT2.bookAvailability(3, 2); // 2021-03-28 9-10

        BookingID testBookID1 = new BookingID(1,1,"LT1",10, 12);
        BookingID testBookID2 = new BookingID(2,3,"LT2",9,10);
        BookingIDArrayList.add(testBookID1);
        BookingIDArrayList.add(testBookID2);

        Control control = new Control();
        Server1_Boundary server1_boundary = new Server1_Boundary();
        Server2_Boundary server2_boundary = new Server2_Boundary();
        Server3_Boundary server3_boundary = new Server3_Boundary();
        Server5_Boundary server5_boundary = new Server5_Boundary();
        Server6_Boundary server6_boundary = new Server6_Boundary();



        while (true)
        {
            byte[] dataTobeUnmarshal = control.receive();
            //todo: handle ack
            if(!handleACK(dataTobeUnmarshal)){
                control.clearDataToBeUnMarshal();
                System.err.println("receive nack");
                continue;
            }
            int serviceID = control.getServiceID_receive();

            switch (serviceID){
                case 1:
                    control.clearDataToBeUnMarshal();
                    server1_boundary.processRequest(dataTobeUnmarshal, facilityArrayList);
                    server1_boundary.clearQueryInfo();
                    break;
                case 2:
                    control.clearDataToBeUnMarshal();
                    server2_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
                    server2_boundary.clearTimeSlotInfo();
                    break;
                case 3:
                    control.clearDataToBeUnMarshal();
                    server3_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
                    break;
                case 4:
                    break;
                case 5:
                    control.clearDataToBeUnMarshal();
                    server5_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
                    break;
                case 6:
                    control.clearDataToBeUnMarshal();
                    server6_boundary.processRequest(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
                    break;
                default:
                    System.out.println("[Server APP] ~~~Unexpected case!!!!");
                    break;
            }
        }
    }

    public static boolean handleACK(byte[] receivedData) {
//        for(byte data: unMarShalData) {
//            System.err.println("marshal: " + unMarShalData);
//        }
        try {
            int isAck = UnMarshal.unmarshalInteger(receivedData, 0);
            //todo: step1: if its nack
            if (isAck == 0) {
                int msgID = UnMarshal.unmarshalInteger(receivedData, 4);
                int status = UnMarshal.unmarshalInteger(receivedData, 8);
                if (status == 0) {
                    byte[] preProcess = ProcessedTable.get(msgID);
                    //todo step2: check in the hashtable
                    if (preProcess == null) {
                        //todo step3: if not found: return nack
                        ArrayList<Object> ackData = new ArrayList<>();
                        ackData.add(0);
                        UDPserver.getInstance().UDPsend(marshalMsgData(ackData, true));
                        return false;
                    }
                    UDPserver.getInstance().UDPsend(preProcess);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
//    private static byte[] checkInTable(int msgID) {
//        return new byte[0];
//    }
}
