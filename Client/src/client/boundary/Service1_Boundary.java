package client.boundary;
import client.control.Service1Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class Service1_Boundary extends Boundary {
    private Service1Control s1C;

    public Service1_Boundary() throws SocketException, UnknownHostException {
        this.s1C = new Service1Control();
    }

    @Override
    public void displayMain(){
        enterFacilityName();
        enterNumOfDays();
        try {
            // marshal
            s1C.marshal();
        }catch (TimeoutException | IOException te){
            System.err.println(te.getMessage());
        }
        //handle response and display reply
        displayReply();
    }

    @Override
    public void displayReply() {

        //todo
        String response = s1C.unMarshal();
        if(response!=null){
            System.out.println("Available intervals: " + response);
        }
    }

    private void enterFacilityName() {
        String name = readInputString("Enter Facility Name: ");
        s1C.setFacName(name);
    }

    private void enterNumOfDays(){
        int days = readInputInteger("Enter the number of days: ");
        s1C.setNumOfDays(days);
    }
}
