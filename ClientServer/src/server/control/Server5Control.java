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

import static server.control.Control.msgIDresponseMap;

public class Server5Control extends ControlFactory{
    private int facilityType;
    private BookingID newBookingID;
    private String chosenFacilityName;
    private int chosenDay;
    private boolean hasVacancy;
    private int startIndex; // endIndex = startIndex+1

    public Server5Control() throws SocketException, UnknownHostException {
        super();
        this.hasVacancy = false;
        this.startIndex = 0;
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
        if (this.dataToBeUnMarshal.length != 0)
        {
            this.facilityType = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16);
            System.err.println("facility type: " + this.facilityType);
            System.out.println("[Server5] --unMarshal-- received facility type is: "+this.facilityType);
            wiseBooking(facilityType, facilityArrayList);
            if (hasVacancy)
            {
                System.out.println("[Server5] --unMarshal-- Chose a vacancy : Facility"+this.chosenFacilityName+" StartIndex: "+this.startIndex);
                // create a new bookingID
                newBookingID = new BookingID(BookingIDArrayList.size()+1, this.chosenDay, this.chosenFacilityName, this.startIndex+7, this.startIndex+8);
                BookingIDArrayList.add(newBookingID);
            }
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
            System.out.println("[Server5]   --marshalAndSend--    Received ACK msg");
        }else{
            if (this.hasVacancy){
                this.marshaledData = Marshal.marshalString(this.newBookingID.getBookingInfoString());
                this.status = new byte[]{0,0,0,1};
               // send(this.marshaledData);
                send(this.marshaledData, Marshal.marshalString(getLatestQueryInfo(7, this.chosenFacilityName)), this.chosenFacilityName, msgID);
            }else{
                this.marshaledData = Marshal.marshalString("There is no vacancy in tomorrow. Pls try another time.");
                this.status = new byte[]{0,0,0,0};
                send(this.marshaledData, this.status ,msgID);
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
        System.out.println("[Server3]   --send--    Has Vacancy: "+this.hasVacancy);
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
        System.out.println("[Server3]   --send--    Has Vacancy: "+this.hasVacancy);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, status, sendData);
        udpSever.UDPsend(addAck_msg);
        //update table
        msgIDresponseMap.put(msgID, addAck_msg);
    }

    /**
     * Wise Booking
     * @param facilityType
     * @param facilityArrayList
     */
    public void wiseBooking(int facilityType, ArrayList<Facility> facilityArrayList){
        System.out.println("Facility Type is: "+facilityType);
        // check LT1 and LT2
        int day =1;
        int a, b;
        for (int i = 1; i < 10; i++) {
            if (facilityType == 1){a =0; b=1;}else{a=2;b=3;}
            if(facilityArrayList.get(a).checkAvailability(day,i))
            { // LT1
                System.out.println("Facility Name: "+facilityArrayList.get(a).getFacilityName());
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(a).bookAvailability(day,i);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(a).getFacilityName();
                break;
            } else if (facilityArrayList.get(b).checkAvailability(day,i))
            { // LT2
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(b).bookAvailability(day,i);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(b).getFacilityName();
                break;
            }else{
                hasVacancy=false;
            }
        } // end inner for loop
    }
}
