package server.FacilityEntity;

import server.control.Server2ControlTest;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server2_Boundary {
    private Server2ControlTest server2;

    public Server2_Boundary() throws SocketException, UnknownHostException {
        this.server2 = new Server2ControlTest();
    }


    public void processRequest(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException{
        server2.unMarshal(facilityArrayList, BookingIDArrayList);
        reply();
    }

    public void reply() throws IOException, TimeoutException {
        server2.marshal();
    }
}
