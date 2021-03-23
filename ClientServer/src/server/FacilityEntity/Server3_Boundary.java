package server.FacilityEntity;

import server.control.Server3ControlTest;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server3_Boundary {
    private Server3ControlTest server3;

    public Server3_Boundary() throws SocketException, UnknownHostException{
        this.server3 = new Server3ControlTest();
    }


    public void processRequest(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException {
        server3.unMarshal(facilityArrayList, BookingIDArrayList);
        reply();
    }

    public void reply() throws IOException, TimeoutException {
        server3.marshal();
    }


}
