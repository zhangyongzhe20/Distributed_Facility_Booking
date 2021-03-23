package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server2ControlTest extends ControlTest {
    private String facilityName;
    private String bookingRequirement;
    private String timeSlots ="";

    private int slotStartIndex;
    private int slots = 0;
    private int day;
    private int bookedFacilityID;
    private BookingID newBookingID;
    private boolean successBooking = false;


    public Server2ControlTest() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.ControlID = 2;
    }

    public String unMarshal(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException{
        receive();
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

    public void marshal() throws TimeoutException, IOException
    {
        if (successBooking)
        {
            System.out.println("Marshal: "+this.newBookingID.getBookingInfoString());
            this.marshaledData = Marshal.marshalString(Integer.toString(this.newBookingID.getID())+this.newBookingID.getBookingInfoString());
            send(this.marshaledData);
        }
        else
        {
            this.marshaledData = Marshal.marshalString("There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
            send(this.marshaledData);
        }
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

}
