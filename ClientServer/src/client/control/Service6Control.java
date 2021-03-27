package client.control;

import utils.UnMarshal;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Service6Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 6;
    private int bookingID;

    public Service6Control() throws SocketException, UnknownHostException {
        super();
        this.collectedData = new ArrayList<>();
        this.marShalData = new byte[0];
        this.unMarShalData = new byte[0];
    }

    /**
     *  Marshal service data
     */
    public void marshal() throws Exception {
            // header represents this is a request/response msg
            collectedData.add(DataMSG);
            // message id
            collectedData.add(this.getMsgID());
            collectedData.add(SERVICEID);
            collectedData.add(bookingID);
            System.out.println("data collect of service6: " + collectedData);
            marShalData = marshalMsg(collectedData, false);
            sendAndReceive(marShalData);
            collectedData = new ArrayList<>();
    }

    public int unMarshal() {
        int actual_data = -1;
        if(this.unMarShalData.length != 0) {
            int isAck = UnMarshal.unmarshalInteger(this.unMarShalData, 0);
            System.out.println(isAck);
            if (isAck == 0) {
                System.err.println("Unsupported operation!!!");
                return -2;
            }
            // actual data
            actual_data = UnMarshal.unmarshalInteger(this.unMarShalData, 4);
            System.out.println(actual_data);
        }
        return actual_data;
    }

    public void setBookingID(int id) {
        this.bookingID = id;
    }

}
