package server.control;

import utils.Marshal;
import utils.UnMarshal;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class Server1Control extends Control{
    private String bookingInfo = "success booking";

    public Server1Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    public String unMarshal() {
        if (this.dataToBeUnMarshal.length != 0)
        {
            int realmsg_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            String realmsg = UnMarshal.unmarshalString(this.dataToBeUnMarshal,28, 28+realmsg_length);
            return realmsg;
        }
        return null;
    }

    public void Marshal() throws TimeoutException, IOException
    {
        this.marshaledData = Marshal.marshalString(bookingInfo);
        sendAndReceive(this.marshaledData);
    }
}
