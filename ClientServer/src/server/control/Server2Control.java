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

import static server.control.Control.msgIDresponseMap;

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

    /**
     * Unmarshal Msg received from server
     * @param dataToBeUnMarshal
     * @param BookingIDArrayList
     * @return
     * @throws IOException
     */
    @Override
    public String unMarshal(byte[] dataToBeUnMarshal, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataToBeUnMarshal;
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 12);
            this.facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 16, 16 + facilityName_length);

            int bookingRequirement_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16+facilityName_length);
            this.bookingRequirement = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 20+facilityName_length, 20+facilityName_length+bookingRequirement_length);

            bookFacility(facilityArrayList);
            if (hasVacancy == 4){
                newBookingID = new BookingID(BookingIDArrayList.size()+1, this.day, this.facilityName,
                        this.slotStartIndex, this.slotStartIndex+slots);
                BookingIDArrayList.add(newBookingID);
            }
            return facilityName;
        }
        return null;
    }

    /**
     * Send Marshaled Data corresponding to each 4 cases to Client
     * @throws TimeoutException
     * @throws IOException
     */
    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0) == 0){
            // Msg Type is ACK
            System.out.println("[Server2]   --marshalAndSend--    Received ACK msg");
        }else{
            int msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
            System.out.println("[Server2]   --marshalAndSend--  Msg Type is request");
            if (hasVacancy==1){ // fully has no vacancy
                System.out.println("[Server2]   --marshalAndSend--  The facility is fully not available in selected time period.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The facility is fully not available in selected time period.");
                this.status = new byte[]{0,0,0,0};
                //send(this.marshaledData);
                send(this.marshaledData, this.status, msgID);
            }
            else if (hasVacancy == 2){ // partial vacancy 2nd half cannot
                System.out.println("[Server2]   --marshalAndSend--  The facility is partially not available in selected time period.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The facility is partially not available in selected time period. You can postpone one slot.");
                this.status = new byte[]{0,0,0,0};
                //send(this.marshaledData);
                send(this.marshaledData, this.status, msgID);
            }
            else if (hasVacancy == 3){ // partial vacancy 1st half cannot
                System.out.println("[Server2]   --marshalAndSend--  The facility is partially not available in selected time period.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The facility is partially not available in selected time period. You can shift one slot advance.");
                this.status = new byte[]{0,0,0,0};
                //send(this.marshaledData);
                send(this.marshaledData, this.status, msgID);
            }
            else if (hasVacancy == 4){
                System.out.println("[Server2]   --marshalAndSend--  Marshal: "+this.newBookingID.getBookingInfoString());
                this.marshaledData = Marshal.marshalString(this.newBookingID.getBookingInfoString());
                this.status = new byte[]{0,0,0,1};
                // from control factory
                send(this.marshaledData, Marshal.marshalString(getLatestQueryInfo(7, this.facilityName)), this.facilityName ,msgID);
            }
            this.hasVacancy = -1;
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    /**
     * Book the client requested facility
     * @param facilityArrayList
     */
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
                /**
                 * If user only required book 1 hour slot
                 */
                if (this.slots == 1) {
                    for (int i = 1; i < this.bookingRequirement.length(); i++) {
                        if (this.bookingRequirement.charAt(i) == '0') {
                            if (!f.checkAvailability(day, i)) {
                                System.out.println("check slot: "+f.checkAvailability(day, i));
                                this.hasVacancy = 1;
                            } // fully no vancancy when booking only one slot\
                            else{
                                f.bookAvailability(day, i);
                                this.hasVacancy = 4;
                                this.slotStartIndex = (i + 7);
                            }
                            break;
                        }
                    }
                    /**
                     * If user required book 2 hour slots; There are 4 cases in total.
                     */
                } else {
                    for (int i = 1; i < this.bookingRequirement.length(); i++) {
                        if (this.bookingRequirement.charAt(i) == '0') {
                            if (f.checkAvailability(this.day, i) && f.checkAvailability(this.day, i+1)){
                                f.bookAvailability(day,i);
                                f.bookAvailability(day,i+1);
                                this.hasVacancy = 4;
                            } // fully available
                            else if (f.checkAvailability(this.day, i) && !f.checkAvailability(this.day, i+1)){
                                this.hasVacancy = 3; // partial vacancy 2nd half cannot
                            }else if (!f.checkAvailability(this.day, i) && f.checkAvailability(this.day, i+1)){
                                this.hasVacancy = 2; // partial vacancy first half cannot
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

    /**
     * Send Data
     * @param sendData
     * @throws IOException
     */
    @Override
    public void send(byte[] sendData) throws IOException{
        System.out.println("[Server2]   --send--    Has Vacancy: "+this.hasVacancy);
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
        System.out.println("[Server1]   --send--    Success query: "+this.hasVacancy);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, status, sendData);
        udpSever.UDPsend(addAck_msg);
        //update table
        msgIDresponseMap.put(msgID, addAck_msg);
    }

//    public void send(byte[] sendData, String facilityName) throws IOException{
//            System.out.println("[Server2]   --send--    Has Vacancy: "+this.hasVacancy);
//            this.ackType = new byte[]{0,0,0,1};
//            byte[] addAck_msg = concat(ackType, this.status, sendData);
//            udpSever.UDPsend(addAck_msg);
//            //notify
//            CallBack.notify(facilityName, addAck_msg);
//    }
}
