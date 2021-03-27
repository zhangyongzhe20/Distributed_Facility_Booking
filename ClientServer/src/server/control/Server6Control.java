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

public class Server6Control extends ControlFactory{
    private int bookingID;
    private boolean bookingIDExist;
    private boolean successcancel;

    private String receivedBookingInfo;
    private int day;
    private int facilityID;
    private int startIndex;
    private int endIndex;
    MonthDateParser mp = new MonthDateParser();

    public Server6Control() throws SocketException, UnknownHostException {
        super();
        this.bookingIDExist = false;
        this.successcancel = false;
    }

    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (this.dataToBeUnMarshal.length != 0) {
            int bookingID_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            System.out.println("BookingID Length: " + bookingID_length);

            this.bookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28);
            System.out.println("BookingID: " + bookingID);

            cancelBooking(facilityArrayList, BookingIDArrayList);

        }
        return null;
    }

    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4) == 0){
            // Msg Type is ACK
            System.out.println("[Server3]   --marshalAndSend--  Received ACK msg");
        }else {
            System.out.println("[Server3]   --marshalAndSend--  Msg Type is request");
            if (!this.bookingIDExist){
                System.out.println("[Server3]   --marshalAndSend-- The BookingID dose not exist.");
                this.marshaledData = Marshal.marshalString("The facility does not exist");
                send(this.marshaledData);
            } else if (this.successcancel){
                System.out.println("[Server3]   --marshalAndSend-- The change booking is success.");
                this.marshaledData = Marshal.marshalString("The cancel is success");
                send(this.marshaledData);
            }
        }
        this.bookingIDExist = false;
        this.successcancel = false;
    }

    public void cancelBooking(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList){
        for (BookingID bid: BookingIDArrayList){
            if (bid.getID() == this.bookingID){
                this.bookingIDExist = true;
                bid.cancelBooking();
                System.out.println("[Server3 Control]   --cancelBooking-- Set BID to cancel");
                this.receivedBookingInfo = bid.getBookingInfoString();
                parseBookingInfo(receivedBookingInfo);
                this.successcancel = true;
                break;
            }else {
                System.out.println("[Server3 Control]   --ChangeBookingSlot-- The BookingID dosenot exist ");
            }
        }

        for (Facility fc: facilityArrayList){
            if (fc.getFacilityID() == this.facilityID){
                for (int i = 0; i < (this.endIndex-this.startIndex); i++) {
                    fc.cancelBooking(this.day, this.startIndex+i);
                    System.out.println("[Server3 Control]   --cancelBooking-- Set Facility to available");
                }
            }
        }

    }

    public void parseBookingInfo(String bookingInfo){
        this.day = StringDayToInt(bookingInfo.substring(6,8)) - mp.getDate();
        this.facilityID = Integer.parseInt(bookingInfo.substring(8,9));
        this.startIndex = Integer.parseInt(bookingInfo.substring(9,11))-7;
        this.endIndex = Integer.parseInt(bookingInfo.substring(11,13))-7;

        System.out.println("[Server3 Control]   --parseBooking Info day: "+day+" facilityID: "+facilityID
                +"  Start Index: "+startIndex+"  End index: "+endIndex);
    }

    public static int StringDayToInt(String day){
        if (day.charAt(0) == '0'){
            return Integer.parseInt(day.substring(1));
        }else
            return Integer.parseInt(day);
    }

}
