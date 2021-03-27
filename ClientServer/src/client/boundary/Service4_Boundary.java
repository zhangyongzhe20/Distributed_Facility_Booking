package client.boundary;

import client.control.Service4Control;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;


public class  Service4_Boundary extends Boundary {
    private Service4Control s4C;

    public Service4_Boundary() throws SocketException, UnknownHostException {
        this.s4C = new Service4Control();
    }

    @Override
    public void displayMain() throws Exception {
        enterFacilityName();
        enterMonitorIntervals();

        //marshal
        s4C.marshal();
        // catch the socket timeout exception to exist the monitoring loop
        // this socket timeout >= interval
        try {
            //todo: need testing
        while(true) {
                s4C.monitoring();
                if (s4C.isNewUpdate){
                    //handle response and display reply
                    displayReply();
                    s4C.isNewUpdate = false;
                }
            }
        }catch (IOException e) {
            System.err.println(e.getMessage());
            // restore the timeout to be the default
            s4C.setInterval(s4C.maxTimeout);
        }
    }



    @Override
    public void displayReply() {
        //todo
        System.out.println("Available intervals: " + s4C.unMarshal());
    }

    private void enterFacilityName() {
        String name = readInputString("Enter the Facility name that you want to monitor: ");
        s4C.setFacName(name);
    }
    private void enterMonitorIntervals() {
        int interval = readInputInteger("Enter your how long you want to monitor the facility (in seconds): ");
        s4C.setInterval(interval);
    }
}
