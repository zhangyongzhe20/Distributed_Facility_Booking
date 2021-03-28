package server.FacilityEntity;

import server.control.Server2Control;
import server.control.Server6Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server6_Boundary implements ServerBoundary{
    private Server6Control server6;

    public Server6_Boundary() throws SocketException, UnknownHostException {
        this.server6 = new Server6Control();
    }

    @Override
    public void processRequest(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException {
        System.out.println("[Server6_Boundary] --processRequest--  Server6 process request");
        server6.unMarshal(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
        reply();
    }

    @Override
    public void reply() throws IOException, TimeoutException {
        server6.marshalAndSend();
    }
}
