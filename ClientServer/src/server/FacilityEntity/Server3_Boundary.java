package server.FacilityEntity;

import server.control.Server3Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server3_Boundary implements ServerBoundary{
    private Server3Control server3;

    public Server3_Boundary() throws SocketException, UnknownHostException {
        this.server3 = new Server3Control();
    }

    @Override
    public void processRequest(byte[] dataTobeUnmarshal, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException {
        System.out.println("Server3 process request");
        server3.unMarshal(dataTobeUnmarshal, BookingIDArrayList);
        reply();
    }

    @Override
    public void reply() throws IOException, TimeoutException {
        server3.marshalAndSend();
    }


}
