package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;

        if (Arrays.equals(Arrays.copyOfRange(this.dataToBeUnMarshal,0,4), new byte[]{9, 9, 9, 9})){
            System.err.println("[Server4]   --unMarshal--   The message has already been processed.");
            this.processed = true;
        }
        if ((!this.processed) && (this.dataToBeUnMarshal.length != 0))
        {
            this.facilityType = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16);
            System.out.println("[Server5] --unMarshal-- received facility type is: "+this.facilityType);
            wiseBooking(facilityType, facilityArrayList);
            if (hasVacancy)
            {
                System.out.println("[Server5] --unMarshal-- Chose a vacancy : Facility"+this.chosenFacilityName+" StartIndex: "+this.startIndex);
                // create a new bookingID
                newBookingID = new BookingID(BookingIDArrayList.size()+1, this.chosenDay, this.chosenFacilityName, this.startIndex+7, this.startIndex+9);
                BookingIDArrayList.add(newBookingID);
            }
        }
        return null;
    }


    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        int msgID;
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0) == 0){
            // Msg Type is ACK
            System.out.println("[Server5]   --marshalAndSend--    Received ACK msg");
        } else if (this.processed){
            // MsgID is in table. It has been processed already.
            System.err.println("[Server1]   --marshalAndSend--   The message has already been processed. Extract from table.");
            msgID = Arrays.copyOfRange(this.dataToBeUnMarshal,4,5)[0];
            System.out.println("MSG ID: "+msgID);
            send(msgIDresponseMap.get(msgID));
        }
        else{
            if (this.hasVacancy){
                this.status = new byte[]{0,0,0,1};
                this.marshaledData = concat(this.status, Marshal.marshalString(this.newBookingID.getBookingInfoString()));
            }else{
                this.status = new byte[]{0,0,0,0};
                this.marshaledData = concat(this.status, Marshal.marshalString("There is no vacancy in tomorrow. Pls try another time."));
            }
            msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
            System.err.println("[Server5]   --marshalAndSend-- Add msgID : "+msgID+" to the table.");
            msgIDresponseMap.put(msgID, marshaledData);
            send(this.marshaledData);
        }
    }

    @Override
    public void send(byte[] sendData) throws IOException{
        System.out.println("[Server3]   --send--    Has Vacancy: "+this.hasVacancy);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, sendData);
        udpSever.UDPsend(addAck_msg);
    }

    public void wiseBooking(int facilityType, ArrayList<Facility> facilityArrayList){
        int fcIndex = facilityType-1;
        // check LT1 and LT2
        int day =1;
        for (int i = 1; i < 10; i++) {
            if(facilityArrayList.get(0+fcIndex).checkAvailability(day,i) &&facilityArrayList.get(0+fcIndex).checkAvailability(day,i+1))
            { // LT1
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(0+fcIndex).bookAvailability(day,i);
                facilityArrayList.get(0+fcIndex).bookAvailability(day,i+1);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(0+fcIndex).getFacilityName();
                break;
            } else if (facilityArrayList.get(1+fcIndex).checkAvailability(day,i) && facilityArrayList.get(1+fcIndex).checkAvailability(day,i+1))
            { // LT2
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(1+fcIndex).bookAvailability(day,i);
                facilityArrayList.get(1+fcIndex).bookAvailability(day,i+1);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(1+fcIndex).getFacilityName();
                break;
            }else{
                hasVacancy=false;
            }
        } // end inner for loop
    }
}
