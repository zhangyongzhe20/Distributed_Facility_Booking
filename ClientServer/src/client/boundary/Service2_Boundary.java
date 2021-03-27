package client.boundary;

import client.control.Service2Control;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Service2_Boundary extends Boundary {
    private Service2Control s2C;

    public Service2_Boundary() throws SocketException, UnknownHostException {
        this.s2C = new Service2Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterFacilityName();
        
        enterDate();
        int start = enterStartTime();
        int end = 0;
        do{
            end = enterEndTime();
        }while(!checkTimeBound(start, end));
        // marshal
        s2C.marshal();
        //handle response and display reply
        displayReply();

    }
    @Override
    public void displayReply() {
        if(s2C.unMarshal() != null)
        System.out.println("Your booking is successful!\n" +
                "Your booking ID: " + s2C.unMarshal().substring(0,1));
       // System.out.println("Your booking Info: " + s2C.unMarshal().substring(1));
    }

    private void enterFacilityName() {
        String name = readInputFacility("Enter Facility Name: ");
        s2C.setFacName(name);
    }

    private void enterDate() {
        String dateOffset = readInputDate("Enter the date (yyyy-mm-dd): ");
//        System.out.println("You must enter the date up to 7 days away from today");
        s2C.setDateOffset(dateOffset);
    }

    private int enterStartTime() {
        int start =  readInputTime("Enter the starting time (eg. 11): ");
        s2C.setStartTime(start);
        return start;
    }

    private int enterEndTime() {
        int end = readInputTime("Enter the ending time (eg. 13): ");
//        System.out.println("Only can book a facility up to 2 hours");
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
        if((end - start) <= 0 || (end - start) > 2){
            System.err.println("You can book a facility up to 2 hours");
            return false;
        }
        return true;
    }
}
