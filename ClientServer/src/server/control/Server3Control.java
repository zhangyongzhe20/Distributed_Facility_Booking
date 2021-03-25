package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.UnMarshal;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server3Control extends ControlFactory{
    private int bookingID;
    private int offset;

    private boolean bookingIDExist;
    private String receivedBookingInfo;

    public Server3Control() throws SocketException, UnknownHostException {
        super();
        this.bookingIDExist = false;
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

            ChangeBookingSlot(bookingID, offset, facilityArrayList, BookingIDArrayList);
        }
        return null;
    }

    public void ChangeBookingSlot(int BookingID, int offset, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList)
    {
        for (BookingID bid: BookingIDArrayList){
            if (bid.getID() == this.bookingID)
            {
                bookingIDExist = true;
                this.receivedBookingInfo = bid.getBookingInfoString();
                System.out.println("[Server3 Control] --ChangeBookingSlot-- "+receivedBookingInfo);

                parseBookingInfo(receivedBookingInfo);
            }
        }
    }

    public static void parseBookingInfo(String bookingInfo){
        String day = bookingInfo.substring(6,8);
        char facilityID = bookingInfo.charAt(9);
        String startIndex = bookingInfo.substring(10,12);
        String endIndex = bookingInfo.substring(12,14);

        System.out.println("day: "+day);
        System.out.println("facilityID: "+facilityID);
        System.out.println("Start Index: "+startIndex);
        System.out.println("End index: "+endIndex);
    }



}
