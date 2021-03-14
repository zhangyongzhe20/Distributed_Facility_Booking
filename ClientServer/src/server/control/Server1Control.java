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
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    public String unMarshal() {
        if (this.dataToBeUnMarshal.length != 0)
        {
            System.out.println("This is a request message");
            String unmarshaledString =  UnMarshal.unmarshalString(this.dataToBeUnMarshal, 4, this.dataToBeUnMarshal.length);
            System.out.println("The unmarshaled String is: " + unmarshaledString);
        }
        return null;
    }

    public void Marshal() throws TimeoutException, IOException
    {
        System.out.println(bookingInfo);
        this.marshaledData = Marshal.marshalString(bookingInfo);
        sendAndReceive(this.marshaledData);
    }
}
