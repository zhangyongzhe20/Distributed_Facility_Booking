package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;
import utils.MonthDateParser;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static client.config.Constants.RESFRATE;
import static server.FacilityEntity.ServerApp.*;
import static server.control.Control.msgID;
import static server.control.Control.setServiceID;

public class Server6Control extends ControlFactory implements ControlChangeFactory{
    private int bookingID;
    private boolean bookingIDExist;

    private String receivedBookingInfo;
    private int day;
    private String facilityName;
    private int startIndex;
    private int endIndex;
    private MonthDateParser mp;

    public Server6Control() throws SocketException, UnknownHostException {
        super();
        this.bookingIDExist = false;
        mp = new MonthDateParser();
    }

    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (this.dataToBeUnMarshal != null) {
            int bookingID_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 12);
            System.out.println("BookingID Length: " + bookingID_length);

            this.bookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 12 + bookingID_length);
            System.out.println("BookingID: " + bookingID);
            System.err.println("executeeeee");
            setServiceID(0);
            cancelBooking(facilityArrayList, BookingIDArrayList);
        }
        return null;
    }

    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        System.err.println("check: " + UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0));
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0) == 0){
            // todo: Msg Type is ACK, delete entry in hashtable
            System.out.println("[Server6]   --marshalAndSend--  Received ACK msg");
        }else {
            System.out.println("[Server6]   --marshalAndSend--  Msg Type is request");
            if(!this.bookingIDExist){
                System.out.println("[Server6]   --marshalAndSend-- The BookingID dose not exist.");
                this.marshaledData = Marshal.marshalString("The BookingID does not exist");
                this.status = new byte[]{0,0,0,0};
            } else {
                System.out.println("[Server6]   --marshalAndSend-- The change booking is success.");
                this.status = new byte[]{0,0,0,1};
                this.marshaledData = Marshal.marshalString("The cancel is success");
            }
            //todo: update processedTable
            System.err.println("update table" + msgID);
            if(ProcessedTable.get(msgID) == null) {
                ProcessedTable.put(msgID, this.marshaledData);
            }
            // simulate this sent message is lost during transmission
            if (Math.random() < RESFRATE) {
                System.out.println("Simulate response message is lost during transmission");
            } else {
                sendResponse(this.marshaledData);
            }
        }
        this.bookingIDExist = false;
    }

    public void cancelBooking(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList){
        for (BookingID bid: BookingIDArrayList){
            if (bid.getID() == this.bookingID){
                if (!bid.isCancel()){
                    this.bookingIDExist = true;
                    bid.cancelBooking();
                    System.out.println("[Server6 Control]   --cancelBooking-- Set BID to cancel");
                    this.receivedBookingInfo = bid.getBookingInfoString();
                    parseBookingInfo(receivedBookingInfo);
                }
                break;
            }else {
                System.out.println("[Server6 Control]   --ChangeBookingSlot-- The BookingID dosenot exist ");
            }
        }

        for (Facility fc: facilityArrayList){
            if (fc.getFacilityName().equals(this.facilityName)){
                for (int i = 0; i < (this.endIndex-this.startIndex); i++) {
                    fc.cancelBooking(this.day, this.startIndex+i);
                    System.out.println("[Server6 Control]   --cancelBooking-- Set Facility to available");
                }
                break;
            }
        }
    }

    public void parseBookingInfo(String bookingInfo){
        this.day = mp.StringDayToInt(bookingInfo.substring(9,11)) - mp.getDate();
        this.facilityName = bookingInfo.substring(12,15);
        this.startIndex = Integer.parseInt(bookingInfo.substring(16,18))-7;
        this.endIndex = Integer.parseInt(bookingInfo.substring(18,20))-7;

        System.out.println("[Server6 Control]   --parseBooking Info day: "+day+" facilityName: "+ facilityName
                +"  Start Index: "+startIndex+"  End index: "+endIndex);
    }


    @Override
    public void sendResponse(byte[] sendData) throws IOException{
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, this.status, sendData);
        udpSever.UDPsend(addAck_msg);
    }

}
