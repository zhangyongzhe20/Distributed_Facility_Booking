package client.boundary;

import client.control.Service2Control;
import java.net.SocketException;
import java.net.UnknownHostException;

import static config.Constants.MAX_BOOKING_HOURS;

public class Service2_Boundary extends Boundary {
    private Service2Control s2C;
    private String response;

    public Service2_Boundary() throws SocketException, UnknownHostException {
        this.s2C = new Service2Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterFacilityName();
        
        enterDate();
        int start = enterStartTime();
        int end;
        do{
            end = enterEndTime();
        }while(!checkTimeBound(start, end));
        /**
         * Diff Services contain diff marshalled data
         */
        s2C.marshal();
        try {
            /**
             * integratedProcess: sendAndReceive + handleACK + unmarsall
             */
            response = s2C.integratedProcess();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }
        displayReply();
    }

    @Override
    public void displayReply() {
        if(response != null) {
            System.out.println("Your booking is successful!\n" +
                    "Your booking ID: " + response.substring(0, 2));
            //01-20210333-LT1-1416-0930
            System.out.println("Date: " + response.substring(3, 11));
            System.out.println("Facility: " + response.substring(12, 15));
            System.out.println("Slot time is from " + response.substring(16, 18) + " to " + response.substring(18, 20));
        }
    }

    private void enterFacilityName() {
        String name = readInputFacility("Enter Facility Name: ");
        s2C.setFacName(name);
    }

    private void enterDate() {
        String dateOffset = readInputDate("Enter the date (yyyy-mm-dd): ");
        s2C.setDateOffset(dateOffset);
    }

    private int enterStartTime() {
        int start =  readInputTime("Enter the starting time (eg. 11): ");
        s2C.setStartTime(start);
        return start;
    }

    private int enterEndTime() {
        int end = readInputTime("Enter the ending time (eg. 13): ");
        s2C.setEndTime(end);
        return end;
    }

    /**
     * Only allow users book up to 2 hours every time
     * @param start
     * @param end
     * @return
     */
    private boolean checkTimeBound(int start, int end) {
        if((end - start) <= 0 || (end - start) > MAX_BOOKING_HOURS){
            System.err.println("You can book a facility up to 2 hours");
            return false;
        }
        return true;
    }
}
