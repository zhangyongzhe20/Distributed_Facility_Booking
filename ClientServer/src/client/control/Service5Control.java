package client.control;

import utils.UnMarshal;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
            // header represents this is a request/response msg
            collectedData.add(DataMSG);
            // message id
            collectedData.add(this.getMsgID());
            collectedData.add(SERVICEID);
            collectedData.add(type);
            marShalData = marshalMsg(collectedData, false);
            sendAndReceive(marShalData);
            collectedData = new ArrayList<>();
    }

    public int unMarshal() {
        int actual_data = -1;
        if(this.unMarShalData.length != 0) {
            int isAck = UnMarshal.unmarshalInteger(this.unMarShalData, 0);
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

    public void setType(int type) { this.type = type;}
}
