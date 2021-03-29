package server.FacilityEntity;

import server.control.Control;
import utils.UnMarshal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static server.control.Control.msgIDresponseMap;

public class ServerApp {

    public static void main(String[] args) throws IOException, TimeoutException {


        ArrayList<BookingID> BookingIDArrayList = new ArrayList<>();

        BookingID testBookID1 = new BookingID(1,1,"LT1",10, 12);
        BookingID testBookID2 = new BookingID(2,3,"LT2",9,10);
        BookingIDArrayList.add(testBookID1);
        BookingIDArrayList.add(testBookID2);

        Control control = new Control();
        Server1_Boundary server1_boundary = new Server1_Boundary();
        Server2_Boundary server2_boundary = new Server2_Boundary();
        Server3_Boundary server3_boundary = new Server3_Boundary();
        Server4_Boundary server4_boundary = new Server4_Boundary();
        Server5_Boundary server5_boundary = new Server5_Boundary();
        Server6_Boundary server6_boundary = new Server6_Boundary();

        while (true)
        {

            System.err.println("echo table: " + msgIDresponseMap.keySet());
            byte[] dataTobeUnmarshal = control.receive();
            //todo: interprete to get msg type and msgID
            int msgType = control.getMsgType();
            int msgID = control.getMsgID();
            if(msgType == 0){
                //System.err.println("receive ack msg");
                int status = control.getStatus();
                if(status == 1){
                    //System.err.println("receive ack msg with status 1");
                    msgIDresponseMap.remove(msgID);
                }else {
                    //if processed before
                    if(msgIDresponseMap.get(msgID) != null) {
                        //System.err.println("Send: " + Arrays.toString(msgIDresponseMap.get(msgID)));
                        control.sendResponse(msgIDresponseMap.get(msgID));
                    }else{
                        control.sendNACK();
                    }
                }
                continue;
            }
            else{
                if(msgIDresponseMap.get(msgID) != null){
                    control.sendResponse(msgIDresponseMap.get(msgID));
                    continue;
                }
            }

            // first process
            int serviceID = control.getServiceID_receive();
            switch (serviceID){
                case 1:
                    control.clearDataToBeUnMarshal();
                    server1_boundary.processRequest(dataTobeUnmarshal);
                    server1_boundary.clearQueryInfo();
                    break;
                case 2:
                    control.clearDataToBeUnMarshal();
                    server2_boundary.processRequest(dataTobeUnmarshal, BookingIDArrayList);
                    server2_boundary.clearTimeSlotInfo();
                    break;
                case 3:
                    control.clearDataToBeUnMarshal();
                    server3_boundary.processRequest(dataTobeUnmarshal, BookingIDArrayList);
                    break;
                case 4:
                    control.clearDataToBeUnMarshal();
                    server4_boundary.processRequest(dataTobeUnmarshal);
                    break;
                case 5:
                    control.clearDataToBeUnMarshal();
                    server5_boundary.processRequest(dataTobeUnmarshal, BookingIDArrayList);
                    break;
                case 6:
                    control.clearDataToBeUnMarshal();
                    server6_boundary.processRequest(dataTobeUnmarshal, BookingIDArrayList);
                    break;
                default:
                    System.out.println("[Server APP] ~~~Unexpected case!!!!");
                    break;
            }
        }
    }
}
