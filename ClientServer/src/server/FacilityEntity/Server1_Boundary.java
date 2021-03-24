package server.FacilityEntity;

import server.control.Server1Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;


public class Server1_Boundary {
    private Server1Control server1;

    public Server1_Boundary() throws SocketException, UnknownHostException, IOException, TimeoutException {
        this.server1 = new Server1Control();
    }

    public void processRequest(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList) throws TimeoutException, IOException{
        System.out.println("Server1 process request");
        server1.unMarshal(dataTobeUnmarshal, facilityArrayList);
        reply();
    }

    public void reply() throws IOException, TimeoutException {
        server1.marshalAndSend();
    }

    public void clearQueryInfo(){
        server1.clearQueryInfo();
    }
}
