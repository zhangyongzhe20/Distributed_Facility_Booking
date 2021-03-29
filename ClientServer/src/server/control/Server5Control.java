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
        if (this.dataToBeUnMarshal.length != 0)
        {
            this.facilityType = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28);
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
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4) == 0){
            // Msg Type is ACK
            System.out.println("[Server5]   --marshalAndSend--    Received ACK msg");
        }else{
            if (this.hasVacancy){
                this.marshaledData = Marshal.marshalString(this.newBookingID.getBookingInfoString());
                this.status = new byte[]{0,0,0,1};
                sendResponse(this.marshaledData);
            }else{
                this.marshaledData = Marshal.marshalString("There is no vacancy in tomorrow. Pls try another time.");
                this.status = new byte[]{0,0,0,0};
                sendResponse(this.marshaledData);
            }
        }
    }

    @Override
    public void sendResponse(byte[] sendData) throws IOException{
        System.out.println("[Server3]   --send--    Has Vacancy: "+this.hasVacancy);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, this.status, sendData);
        udpSever.UDPsend(addAck_msg);
    }

    public void wiseBooking(int facilityType, ArrayList<Facility> facilityArrayList){
        int fcIndex = facilityType-1;
        // check LT1 and LT2
        int day =1;
        for (int i = 1; i < 10; i++) {
            if(facilityArrayList.get(0+fcIndex).checkAvailability(day,i))
            { // LT1
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(0+fcIndex).bookAvailability(day,i);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(0+fcIndex).getFacilityName();
                break;
            } else if (facilityArrayList.get(1+fcIndex).checkAvailability(day,i))
            { // LT2
                this.hasVacancy = true;
                this.startIndex = i;
                facilityArrayList.get(1+fcIndex).bookAvailability(day,i);
                this.chosenDay = day;
                this.chosenFacilityName = facilityArrayList.get(1+fcIndex).getFacilityName();
                break;
            }else{
                hasVacancy=false;
            }
        } // end inner for loop
    }
}
