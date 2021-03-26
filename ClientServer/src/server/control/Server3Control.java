package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

public class Server3Control extends ControlFactory{
    private int bookingID;
    private int offset;

    private boolean bookingIDExist;
    private boolean successcancel;
    private boolean noCollision;
    private BookingID changedBookingID;

    private String receivedBookingInfo;
    private int day;
    private int facilityID;
    private int startIndex;
    private int endIndex;

    public Server3Control() throws SocketException, UnknownHostException {
        super();
        this.bookingIDExist = false;
        this.successcancel = false;
        this.noCollision = false;
        this.receivedBookingInfo = "";
    }

    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (this.dataToBeUnMarshal.length != 0)
        {
            int bookingID_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            System.out.println("BookingID Length: "+bookingID_length);

            this.bookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28);
            System.out.println("BookingID: "+bookingID);

            int hours_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 32);
            System.out.println("hours length: "+hours_length);

            this.offset = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 36);
            System.out.println("offset: "+offset);

            ChangeBookingSlot(facilityArrayList, BookingIDArrayList);
            if (noCollision && (!successcancel))
            {
                // create a new bid
                changedBookingID = new BookingID(BookingIDArrayList.size()+1, this.day, this.facilityID ,
                        this.startIndex+offset+7, this.endIndex+offset+7);
                BookingIDArrayList.add(changedBookingID);
                System.out.println("[Server3 Control]   --changeBookingTime-- Add the changed BookingID: \n"+changedBookingID.getBookingInfoString());
            }
        }
        return null;
    }

    public void ChangeBookingSlot(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList)
    {
        for (BookingID bid: BookingIDArrayList){
            // check if the BookID is valid
            if (bid.getID() == this.bookingID)
            {
                if (!bid.isCancel())
                {
                    bookingIDExist = true;
                    this.receivedBookingInfo = bid.getBookingInfoString();
                    parseBookingInfo(receivedBookingInfo);

                    if (this.offset == 0){
                        // choose to cancel booking
                        System.out.println("[Server3 Control]   --CancelBookingSlot-- "+receivedBookingInfo);
                        cancelBooking(facilityArrayList, BookingIDArrayList);
                        this.successcancel = true;
                    }else{
                        // choose to change booking slots
                        System.out.println("[Server3 Control]   --ChangeBookingSlot-- "+receivedBookingInfo);
                        changeBookingTime(facilityArrayList, BookingIDArrayList);
                    }
                }
                else {
                    System.out.println("[Server3 Control]   --ChangeBookingSlot-- The BookingID dosenot exist ");
                }
            }
        }
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
            }
            else if (this.successcancel){
                System.out.println("[Server3]   --marshalAndSend-- The cancel booking is success.");
                this.marshaledData = Marshal.marshalString("Your cancel is success");
                send(this.marshaledData);
            } else if (this.noCollision){
                System.out.println("[Server3]   --marshalAndSend-- The change booking is success.");
                this.marshaledData = Marshal.marshalString(Integer.toString(this.changedBookingID.getID())+this.changedBookingID.getBookingInfoString());
                send(this.marshaledData);
            }
        }
        this.bookingIDExist = false;
        this.successcancel = false;
        this.noCollision = false;
    }

    @Override
    public void send(byte[] sendData) throws IOException{
        if ((this.successcancel && this.bookingIDExist) || this.noCollision){
            this.ackType = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        }
        else if (!this.bookingIDExist){
            this.ackType = new byte[] {0,0,0,-1};
            System.out.println("[Server2]   --send--    send reply to client with ACK = 0");
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        } else if (!this.noCollision){
            this.ackType = new byte[] {0,0,0,0};
            System.out.println("[Server2]   --send--    send reply to client with ACK = 0");
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        }
    }


    public void cancelBooking(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList){
        for (BookingID bid: BookingIDArrayList){
            if (bid.getID() == this.bookingID){
                bid.cancelBooking();
                System.out.println("[Server3 Control]   --cancelBooking-- Set BID to cancel");
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

    public void changeBookingTime(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList){
        for (BookingID bid: BookingIDArrayList){
            if (bid.getID() == this.bookingID){
                bid.cancelBooking();
                System.out.println("[Server3 Control]   --changeBookingTime-- Set previous BID to cancel");
            }
        }
        for (Facility fc: facilityArrayList) {
            if (fc.getFacilityID() == this.facilityID){
                this.noCollision = fc.changeBooking(this.day, this.offset, this.startIndex-1, this.endIndex-1);
                System.out.println("There is collision: "+this.noCollision);
                fc.setPrintSlot(7);
                System.out.println("[Server3 Control]   --changeBookingTime-- The new availability is: \n" + fc.getPrintResult());
            }
        }
    }

    public void parseBookingInfo(String bookingInfo){
        this.day = StringDayToInt(bookingInfo.substring(6,8)) - getCurrentDay();
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

    public static int getCurrentDay() {
        String DATE_FORMAT = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String date = sdf.format(c1.getTime()); // date = date.substring(0,8) currTime = date.substring(8,12)
        return Integer.parseInt(date.substring(6, 8));
    }

}
