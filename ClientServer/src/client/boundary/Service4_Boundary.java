package client.boundary;

import client.control.Service4Control;
import client.control.UDPClient;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static client.config.Constants.MAXTIMEOUTCOUNT;
import static client.config.Constants.UDPTIMEOUT;


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

        s4C.sendOnce();

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
            System.err.println("Monitoring timeouts");
            // restore the timeout to be the default
            UDPClient.getInstance().setTimeOut(UDPTIMEOUT);
        }
    }

    @Override
    public void displayReply() {
        //todo
        try {
            String response = s4C.unMarshal();
            if(response!=null) {
                System.out.println(response);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    private void enterFacilityName() {
        String name = readInputFacility("Enter the Facility name that you want to monitor: ");
        s4C.setFacName(name);
    }
    private void enterMonitorIntervals() {
        int interval = readInputInteger("Enter your how long you want to monitor the facility (in seconds): ");
        s4C.setInterval(interval);
    }
}
