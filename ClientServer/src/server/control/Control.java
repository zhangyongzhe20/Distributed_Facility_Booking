package server.control;

import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static server.FacilityEntity.ServerApp.ProcessedTable;
import static utils.Marshal.marshalMsgData;

public class Control {
    byte[] dataToBeUnMarshal;
    UDPserver udpSever;

    int msgID;
    int serviceID_receive;


    public int getServiceID_receive() {
        return serviceID_receive;
    }

    public Control() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
    }

    public byte[] receive() throws IOException {
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPreceive();
        if(!handleACK(this.dataToBeUnMarshal)){
            clearDataToBeUnMarshal();
            System.err.println("receive nack");
        }
        udpSever.clearRecieveMsg();
        if (!(this.dataToBeUnMarshal.length==0))
        {parse();}
        return this.dataToBeUnMarshal;
    }

    public void parse(){
        this.msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,12);
        this.serviceID_receive = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 20);
        System.out.println("[Control]   --parse--   serviceID:   " + this.serviceID_receive);
    }

    public void clearDataToBeUnMarshal() {
        System.out.println("[Control]   -- clearDataToBeUnMarshal-- Clear Data");
        this.dataToBeUnMarshal = new byte[0];
    }

    public boolean handleACK(byte[] receivedData) {
        try {
            int isAck = UnMarshal.unmarshalInteger(receivedData, 0);
            //todo: step1: if its nack
            if (isAck == 0) {
                int msgID = UnMarshal.unmarshalInteger(receivedData, 4);
                int status = UnMarshal.unmarshalInteger(receivedData, 8);
                if (status == 0) {
                    byte[] preProcess = ProcessedTable.get(msgID);
                    //todo step2: check in the hashtable
                    if (preProcess == null) {
                        //todo step3: if not found: return nack
                        ArrayList<Object> ackData = new ArrayList<>();
                        ackData.add(0);
                        this.udpSever.UDPsend(marshalMsgData(ackData, true));
                        return false;
                    }
                    this.udpSever.UDPsend(preProcess);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
}
