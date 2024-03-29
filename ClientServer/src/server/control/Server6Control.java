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

import static server.control.Control.msgIDresponseMap;

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

    /**
     * Unmarshal Msg received from server
     * @param dataTobeUnmarshal
     * @param BookingIDArrayList
     * @return
     * @throws IOException
     */
    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (this.dataToBeUnMarshal.length != 0) {
            int bookingID_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 12);
            System.out.println("BookingID Length: " + bookingID_length);

            this.bookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16);
            System.out.println("BookingID: " + bookingID);

            cancelBooking(facilityArrayList, BookingIDArrayList);
        }
        return null;
    }

    /**
     * Send Marshaled Data corresponding to each 2 cases to Client
     * @throws TimeoutException
     * @throws IOException
     */
    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        int msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0) == 0){
            // Msg Type is ACK
            System.out.println("[Server6]   --marshalAndSend--  Received ACK msg");
        }else {
            System.out.println("[Server6]   --marshalAndSend--  Msg Type is request");
            if(!this.bookingIDExist){
                System.out.println("[Server6]   --marshalAndSend-- The BookingID dose not exist.");
                this.marshaledData = Marshal.marshalString("The BookingID does not exist");
                this.status = new byte[]{0,0,0,0};
                send(this.marshaledData, this.status, msgID);
            } else {
                System.out.println("[Server6]   --marshalAndSend-- The change booking is success.");
                this.status = new byte[]{0,0,0,1};
                this.marshaledData = Marshal.marshalString("The cancel is success");
                send(this.marshaledData, Marshal.marshalString(getLatestQueryInfo(7, this.facilityName)), this.facilityName, msgID);
            }
            //send(this.marshaledData);
        }
        this.bookingIDExist = false;
    }

    /**
     * Cancel a previous booking
     * @param facilityArrayList
     * @param BookingIDArrayList
     */
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

    /**
     * Parse the received booking information
     * @param bookingInfo
     */
    public void parseBookingInfo(String bookingInfo){
        this.day = mp.StringDayToInt(bookingInfo.substring(9,11)) - mp.getDate();
        this.facilityName = bookingInfo.substring(12,15);
        this.startIndex = Integer.parseInt(bookingInfo.substring(16,18))-7;
        this.endIndex = Integer.parseInt(bookingInfo.substring(18,20))-7;

        System.out.println("[Server6 Control]   --parseBooking Info day: "+day+" facilityName: "+ facilityName
                +"  Start Index: "+startIndex+"  End index: "+endIndex);
    }

    /**
     * Send Data
     * @param sendData
     * @throws IOException
     */
    @Override
    public void send(byte[] sendData) throws IOException{
        System.out.println("[Server2]   --send--    Success Cancel: "+this.bookingIDExist);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, this.status, sendData);
        udpSever.UDPsend(addAck_msg);
    }

    /**
     * Send Data
     * @param sendData
     * @param status
     * @param msgID
     * @throws IOException
     */
    public void send(byte[] sendData, byte[] status, int msgID) throws IOException{
        System.out.println("[Server2]   --send--    Success Cancel: "+this.bookingIDExist);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, status, sendData);
        udpSever.UDPsend(addAck_msg);
        //update table
        msgIDresponseMap.put(msgID, addAck_msg);
    }

}
