package server.FacilityEntity;

import server.control.Server1Control;
import server.control.Server4Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server4_Boundary implements ServerBoundary {
    private Server4Control server4;

    public Server4_Boundary() throws SocketException, UnknownHostException, IOException, TimeoutException {
        this.server4 = new Server4Control();
    }

    public void processRequest(byte[] dataTobeUnmarshal) throws TimeoutException, IOException{
        server4.unMarshal(dataTobeUnmarshal);
    }
}
