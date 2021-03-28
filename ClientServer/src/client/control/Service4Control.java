package client.control;

import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static client.config.Constants.*;

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
            //reset every time
            this.collectedData = new ArrayList<>();
            // header represents this is a request/response msg
            collectedData.add(DataMSG);
            // message id
            collectedData.add(this.getMsgID());
            collectedData.add(SERVICEID);
            collectedData.add(name);
            collectedData.add(interval);
            System.out.println("data collect of service4: " + collectedData);
            marShalData = marshalMsg(collectedData, false);
    }


    public void setFacName(String name) { this.name = name;}


    public void setInterval(int interval) {
        this.interval = interval;
        // to set udp timeout to be the interval
        try {
            UDPClient.getInstance().setTimeOut(interval * 1000);
        } catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(5);
        }

    }

    public void monitoring() throws IOException {
            // if receive update from server
            if ((this.unMarShalData = udpClient.UDPreceive()) != null){
                this.isNewUpdate = true;
            }
    }
}
