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
            String real_data =  UnMarshal.unmarshalString(this.dataToBeUnMarshal, 4, this.dataToBeUnMarshal.length);
            return real_data;
        }
        return null;
    }

    public void Marshal() throws TimeoutException, IOException
    {
        this.marshaledData = Marshal.marshalString(bookingInfo);
        sendAndReceive(this.marshaledData);
    }
}
