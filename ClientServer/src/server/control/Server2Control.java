package server.control;

import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server2Control extends Control{
    private String bookingReceipt = "";
    private String facilityName;
    private String bookingRequirement;
    private String timeSlots ="";

    private int slotStartIndex;
    private int slots = 0;

    public Server2Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.id = 2;
    }

    public String unMarshal(ArrayList<Facility> facilityArrayList) throws IOException{
        receive();
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            this.facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + facilityName_length);

            int bookingRequirement_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28+facilityName_length);
            this.bookingRequirement = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 32+facilityName_length, 32+facilityName_length+bookingRequirement_length);

            bookFacility(facilityArrayList);

            this.bookingReceipt = getBookingReceipt();
            return facilityName;
        }
        return null;
    }

    public void marshal() throws TimeoutException, IOException
    {
        System.out.println("Marshal: "+this.bookingReceipt);
        this.marshaledData = Marshal.marshalString(this.bookingReceipt);
        send(this.marshaledData);
    }

    public void bookFacility(ArrayList<Facility> facilityArrayList){
        for (Facility f: facilityArrayList)
        {
            if (f.getFacilityName().equals(this.facilityName))
            {
                int day = Integer.parseInt(String.valueOf(this.bookingRequirement.charAt(0)));
                for (int i = 1; i < this.bookingRequirement.length(); i++) {
                    if(this.bookingRequirement.charAt(i) == '0')
                    {
                        f.bookAvailability(day, i);
                        this.slotStartIndex = (i+7);
                        slots += 1;
                    }
                }
            }
            f.setPrintSlot(7);
            this.timeSlots += f.getPrintResult();
        }
    }

    public String getBookingReceipt()
    {
        String confirmBook = "Your booking start time is: "+ this.slotStartIndex + "\n"+
                "Your booking end time is: "+ (this.slotStartIndex+this.slots) + "\n"+"Thanks for your booking\n"+
                "The available facilities now are: "+this.timeSlots;
        return confirmBook;
    }






}
