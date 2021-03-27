package client.control;

import utils.UnMarshal;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static utils.CalculateDate.calIndexFromToday;
import static client.config.Constants.*;

public class Service2Control extends Control implements marshal, unmarshal{
    private static final int SERVICEID = 2;
    private String facilityName;
    private int endTime;
    private int startTime;
    private String dateOffset;
    private final String INITSLOTS = "1111111111";

    public Service2Control() throws SocketException, UnknownHostException {
        super();
        this.collectedData = new ArrayList<>();
        this.marShalData = new byte[0];
        this.unMarShalData = new byte[0];
    }

    /**
     * To store dateOffset, startTime, endTime into a String, and wait to be marshalled
     * @return A string contains 11 characters, first char is the dateOffset
     * @throws Exception
     */
    private String processDate(){
        StringBuilder availSlots = new StringBuilder(INITSLOTS);
        for(int i = startTime; i<endTime; i++){
            availSlots.setCharAt(i-8, '0');
        }
        return dateOffset.concat(String.valueOf(availSlots));
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
            collectedData.add(facilityName);
            collectedData.add(processDate());
            marShalData = marshalMsg(collectedData, false);
    }

    public void setFacName(String name) {
        this.facilityName = name;
    }
    public void setDateOffset(String offset) { this.dateOffset = offset;}
    public void setStartTime(int start) { this.startTime = start;}
    public void setEndTime(int end) { this.endTime = end; }

}
