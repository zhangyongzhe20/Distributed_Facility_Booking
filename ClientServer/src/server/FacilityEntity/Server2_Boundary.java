package server.FacilityEntity;

import server.control.Server2Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server2_Boundary {
    private Server2Control server2;

    public Server2_Boundary() throws SocketException, UnknownHostException {
        this.server2 = new Server2Control();
    }


    public void processRequest(ArrayList<Facility> facilityArrayList) throws TimeoutException, IOException{
        server2.unMarshal(facilityArrayList);
        reply();
    }

    public void reply() throws IOException, TimeoutException {
        server2.marshal();
    }
}
