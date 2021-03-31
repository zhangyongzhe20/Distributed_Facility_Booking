package client.boundary;
import client.control.Service1Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


public class Service1_Boundary extends Boundary {
    private Service1Control s1C;
    private String response;

    public Service1_Boundary() throws SocketException, UnknownHostException {
        this.s1C = new Service1Control();
    }

    @Override
    public void displayMain() throws TimeoutException, IOException {
        enterFacilityName();
        enterNumOfDays();
        /**
         * Diff Services contain diff marshalled data
         */
        s1C.marshal();
        try {
            /**
             * integratedProcess: sendAndReceive + handleACK + unmarsall
             */
            response = s1C.integratedProcess();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }
        displayReply();
        s1C.resetUnmarshal();
    }

    @Override
    public void displayReply() {
        if(response!=null){
            System.out.println("Available intervals: " + "\n"+ response);
        }
    }

    private void enterFacilityName() {
        String name = readInputFacility("Enter Facility Name: ");
        s1C.setFacName(name);
    }

    private void enterNumOfDays(){
        int days = readNumOfQueryDays("Enter the number of days: ");
        s1C.setNumOfDays(days);
    }
}
