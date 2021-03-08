package client.control;
import java.util.ArrayList;

public class Service1Control extends Control{
    private String facilityName;
    private int numOfDays;

    public Service1Control() {
        this.collectedData = new ArrayList<>();
        this.marShalData = new byte[0];
        this.marShalHeader = new byte[0];
        this.unMarShalData = new byte[0];
    }

    public void setFacName(String name) {
        this.facilityName = name;
    }

    public void setNumOfDays(int days) {
        this.numOfDays = days;
    }

    @Override
    public void sendAndReceive() {
        //todo integrate UDP transmission here
        this.unMarShalData = sendAndReceive(marShalHeader);
        this.unMarShalData = sendAndReceive(marShalData);
        //todo how to send ack??????????

    }

    @Override
    public void marshal() {
        collectedData.add(this.facilityName);
        collectedData.add(this.numOfDays);
        marShalHeader = marshalMsgHeader(collectedData);
        marShalData = marshalMsg(collectedData);
    }

    @Override
    public void unMarshal() {
        //todo

    }
}
