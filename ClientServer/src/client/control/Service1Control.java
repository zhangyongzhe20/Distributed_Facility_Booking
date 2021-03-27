package client.control;

import utils.UnMarshal;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Service1Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 1;
    private String facilityName;
    private int numOfDays;

    public Service1Control() throws SocketException, UnknownHostException {
        super();
        this.collectedData = new ArrayList<>();
        this.marShalData = new byte[0];
        this.unMarShalData = new byte[0];
    }

    public void setFacName(String name) {
        this.facilityName = name;
    }

    public void setNumOfDays(int days) {
        this.numOfDays = days;
    }

    /**
     *  Marshal service data
     */
    public void marshal() throws TimeoutException, IOException {
            //reset every time
            this.collectedData = new ArrayList<>();
            // header represents this is a request/response msg
            collectedData.add(DataMSG);
            // message id
            collectedData.add(this.getMsgID());
            collectedData.add(SERVICEID);
            collectedData.add(this.facilityName);
            collectedData.add(this.numOfDays);
            marShalData = marshalMsg(collectedData, false);
    }
}
