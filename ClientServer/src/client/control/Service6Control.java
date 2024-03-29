package client.control;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static config.Constants.*;
/**
 * @author Z. YZ
 */
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
            //reset every time
            this.collectedData = new ArrayList<>();
            // header represents this is a request/response msg
            collectedData.add(DataMSG);
            // message id
            collectedData.add(this.getMsgID());
            collectedData.add(SERVICEID);
            collectedData.add(bookingID);
            marShalData = marshalMsg(collectedData, false);
    }

    public void setBookingID(int id) {
        this.bookingID = id;
    }

}
