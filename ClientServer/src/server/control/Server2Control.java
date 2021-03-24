package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server2Control {
    private String facilityName;
    private String bookingRequirement;
    private String timeSlots ="";
    private boolean successBooking = false;
    private int slotStartIndex;
    private int slots = 0;
    private int day;
    private int bookedFacilityID;
    private BookingID newBookingID;

    private byte[] dataToBeUnMarshal;
    private byte[] marshaledData;
    UDPserver udpSever;
    byte[] ackType;

    public Server2Control() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    public void clearTimeSlots(){this.timeSlots="";}

    public String unMarshal(byte[] dataToBeUnMarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataToBeUnMarshal;
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            this.facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + facilityName_length);

            int bookingRequirement_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28+facilityName_length);
            this.bookingRequirement = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 32+facilityName_length, 32+facilityName_length+bookingRequirement_length);

            bookFacility(facilityArrayList);
            if (successBooking)
            {
                newBookingID = new BookingID(BookingIDArrayList.size()+1, this.day, this.bookedFacilityID , this.slotStartIndex, this.slotStartIndex+slots);
                BookingIDArrayList.add(newBookingID);
            }
            return facilityName;
        }
        return null;
    }

    public void marshalAndSend() throws TimeoutException, IOException{
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4) == 0){
            // Msg Type is ACK
            System.out.println("Received ACK msg");
        }else{
            System.out.println("Msg Type is request");
            if (this.successBooking){
                System.out.println("Marshal: "+this.newBookingID.getBookingInfoString());
                this.marshaledData = Marshal.marshalString(Integer.toString(this.newBookingID.getID())+this.newBookingID.getBookingInfoString());
                send(this.marshaledData);
            }else{
                this.marshaledData = Marshal.marshalString("Booking Failed");
                send(this.marshaledData);
            }
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    public void bookFacility(ArrayList<Facility> facilityArrayList){
        for (Facility f: facilityArrayList)
        {
            if (f.getFacilityName().equals(this.facilityName))
            {
                this.successBooking = true;
                this.bookedFacilityID = f.getFacilityID();
                this.day = Integer.parseInt(String.valueOf(this.bookingRequirement.charAt(0)));
                for (int i = 1; i < this.bookingRequirement.length(); i++) {
                    if(this.bookingRequirement.charAt(i) == '0')
                    {
                        f.bookAvailability(this.day, i);
                        this.slotStartIndex = (i+7);
                        slots += 1;
                    }
                }
            }
            f.setPrintSlot(7);
            this.timeSlots += f.getPrintResult();
        }
    }

    public void send(byte[] sendData) throws IOException{
        System.out.println("Success booking: "+this.successBooking);
        if (this.successBooking){
            this.ackType = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        }
        else {
            this.ackType = new byte[] {0,0,0,0};
            System.out.println("send reply to client with ACK = 0");
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        }
    }

    public static byte[] concat(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        byte[] c = baos.toByteArray();
        return c;
    }

}
