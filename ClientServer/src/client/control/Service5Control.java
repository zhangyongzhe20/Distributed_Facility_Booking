package client.control;

import utils.UnMarshal;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static client.config.Constants.*;

public class Service5Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 5;
    private int type;

    public Service5Control() throws SocketException, UnknownHostException {
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
            collectedData.add(type);
            marShalData = marshalMsg(collectedData, false);
    }

    public void setType(int type) { this.type = type;}
}
