package client.control;

import utils.UnMarshal;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static client.config.Constants.*;

public class Service3Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 3;
    private int bookingID;
    private int offset;

    public Service3Control() throws SocketException, UnknownHostException {
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
            collectedData.add(offset);
            System.out.println("data collect of service3: " + collectedData);
            marShalData = marshalMsg(collectedData, false);
    }

    public void setBookingID(int id) {
        this.bookingID = id;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
