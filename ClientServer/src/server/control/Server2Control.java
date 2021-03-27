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

public class Server2Control extends ControlFactory{
    private String facilityName;
    private String bookingRequirement;
    private String timeSlots;

    private int hasVacancy = -1; //

    private int slotStartIndex;
    private int slots = 0;
    private int day;
    private int bookedFacilityID;
    private BookingID newBookingID;


    public Server2Control() throws SocketException, UnknownHostException {
        super();
        this.timeSlots = "";
    }

    public void clearTimeSlots(){this.timeSlots="";}

    @Override
    public String unMarshal(byte[] dataToBeUnMarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataToBeUnMarshal;
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            this.facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + facilityName_length);

            int bookingRequirement_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28+facilityName_length);
            this.bookingRequirement = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 32+facilityName_length, 32+facilityName_length+bookingRequirement_length);

            bookFacility(facilityArrayList);
            if (hasVacancy == 3){
                newBookingID = new BookingID(BookingIDArrayList.size()+1, this.day, this.bookedFacilityID ,
                        this.slotStartIndex, this.slotStartIndex+slots);
                BookingIDArrayList.add(newBookingID);
            }
            return facilityName;
        }
        return null;
    }

    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4) == 0){
            // Msg Type is ACK
            System.out.println("[Server2]   --marshalAndSend--    Received ACK msg");
        }else{
            System.out.println("[Server2]   --marshalAndSend--  Msg Type is request");
            if (hasVacancy==1){ // fully has no vacancy
                System.out.println("[Server2]   --marshalAndSend--  The facility is fully not available in selected time period.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The facility is fully not available in selected time period.");
                send(this.marshaledData);
            }
            else if (hasVacancy == 2){ // partial vacancy
                System.out.println("[Server2]   --marshalAndSend--  The facility is partially not available in selected time period.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The facility is partially not available in selected time period.");
                send(this.marshaledData);
            }
            else if (hasVacancy == 3){
                System.out.println("[Server2]   --marshalAndSend--  Marshal: "+this.newBookingID.getBookingInfoString());
                this.marshaledData = Marshal.marshalString(Integer.toString(this.newBookingID.getID())+this.newBookingID.getBookingInfoString());
                send(this.marshaledData);
            }
            this.hasVacancy = -1;
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    public void bookFacility(ArrayList<Facility> facilityArrayList) {
        boolean vacancy;
        this.slots = 0;
        for (Facility f : facilityArrayList) {
            if (f.getFacilityName().equals(this.facilityName)) {
                this.bookedFacilityID = f.getFacilityID();
                this.day = Integer.parseInt(String.valueOf(this.bookingRequirement.charAt(0)));

                for (int i = 0; i < this.bookingRequirement.length(); i++) {
                    if (this.bookingRequirement.charAt(i) == '0') {
                        this.slots += 1;
                    }
                }
                System.out.println("Number of slots " + this.slots);

                if (this.slots == 1) {
                    for (int i = 1; i < this.bookingRequirement.length(); i++) {
                        if (this.bookingRequirement.charAt(i) == '0') {
                            if (!f.checkAvailability(day, i)) {
                                System.out.println("check slot: "+f.checkAvailability(day, i));
                                this.hasVacancy = 1;
                            } // fully no vancancy when booking only one slot\
                            else{
                                f.bookAvailability(day, i);
                                this.hasVacancy = 3;
                                this.slotStartIndex = (i + 7);
                            }
                            break;
                        }
                    }
                } else {
                    for (int i = 1; i < this.bookingRequirement.length(); i++) {
                        if (this.bookingRequirement.charAt(i) == '0') {
                            if (f.checkAvailability(this.day, i) && f.checkAvailability(this.day, i+1)){
                                f.bookAvailability(day,i);
                                f.bookAvailability(day,i+1);
                                this.hasVacancy = 3;
                            }
                            else if (f.checkAvailability(this.day, i) && !f.checkAvailability(this.day, i+1)){
                                this.hasVacancy = 2; // partial vacancy
                            }else if (!f.checkAvailability(this.day, i) && f.checkAvailability(this.day, i+1)){
                                this.hasVacancy = 2; // partial vacancy
                            }else if (!f.checkAvailability(this.day, i) && !f.checkAvailability(this.day, i+1)){
                                this.hasVacancy = 1; // fully no vacancy
                            }
                            this.slotStartIndex = (i + 7);
                            break;
                        }
                    }
                }
                f.setPrintSlot(7);
                this.timeSlots += f.getPrintResult();
            }
        }
    }

    @Override
    public void send(byte[] sendData) throws IOException{
            System.out.println("[Server2]   --send--    Has Vacancy: "+this.hasVacancy);
            this.ackType = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
    }
}
