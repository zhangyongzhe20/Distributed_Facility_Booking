package server.control;

import Utils.Marshal;
import Utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server1Control extends Control implements marshal, unmarshal{
    private String bookingInfo = "success booking";

    public Server1Control() throws SocketException, UnknownHostException {
        super();
        this.marShalData = new byte[0];
        this.unMarShalData = new byte[0];
    }

    public String unMarshal() {
        if (this.marShalData.length != 0)
        {
            // This is a request message
            return UnMarshal.unmarshalString(this.marShalData, 4, this.marShalData.length);
        }
        return null;
    }

    public void Marshal() throws TimeoutException, IOException
    {
        marShalData = Marshal.marshalString(bookingInfo);
        sendAndReceive(marShalData);
    }
}
