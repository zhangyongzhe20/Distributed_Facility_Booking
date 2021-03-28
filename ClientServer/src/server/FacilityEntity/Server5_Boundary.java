package server.FacilityEntity;

import server.control.Server5Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server5_Boundary implements ServerBoundary{
    private Server5Control server5;

    public Server5_Boundary() throws SocketException, UnknownHostException {
        this.server5 = new Server5Control();
    }

    @Override
    public void processRequest(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException {
        System.out.println("[Server5_Boundary] --processRequest--  Server5 process request");
        server5.unMarshal(dataTobeUnmarshal, facilityArrayList, BookingIDArrayList);
        reply();
    }

    @Override
    public void reply() throws IOException, TimeoutException {
        server5.marshalAndSend();
    }
}
