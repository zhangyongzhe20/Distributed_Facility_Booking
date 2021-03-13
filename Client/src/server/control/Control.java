package server.control;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Control {
    ArrayList<Object> collectedData;
    byte[] marShalData;
    byte[] unMarShalData;
    UDPserver udpSever;

    // 1. unmarshal data
    // 2. Check it is ACK or request.
    // If is ACK, end process; If request check messageID is stored in map or not
    // 3. send message

    public Control() throws SocketException, UnknownHostException {
//        this.udpSever = UDPserver.getInstance();
    }

}
