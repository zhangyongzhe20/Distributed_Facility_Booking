package client.control;

import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Service4Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 4;
    public boolean isNewUpdate;
    private String name;
    private int interval;

    public Service4Control() throws SocketException, UnknownHostException {
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
            collectedData.add(name);
            collectedData.add(interval);
            System.out.println("data collect of service4: " + collectedData);
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

    public void setFacName(String name) { this.name = name;}


    public void setInterval(int interval) {
        this.interval = interval;
        // to set udp timeout to be the interval
        this.maxTimeout = interval;
    }

    public void monitoring() throws IOException {
            // if receive update from server
            if ((this.unMarShalData = udpClient.UDPreceive()) != null){
                this.isNewUpdate = true;
            }
    }
}
