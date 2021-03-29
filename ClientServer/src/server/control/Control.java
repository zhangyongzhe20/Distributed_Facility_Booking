package server.control;

import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import static server.FacilityEntity.ServerApp.ProcessedTable;

public class Control extends ControlFactory{
    byte[] dataToBeUnMarshal;

    static int msgID = 0;
    static int serviceID_receive;

   public static void setServiceID(int id){ serviceID_receive = id;}


    public int getServiceID_receive() {
        return serviceID_receive;
    }

    public Control() throws SocketException, UnknownHostException {
//        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
    }

    public byte[] receive() throws IOException {
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPreceive();
        System.err.println("receive: " + Arrays.toString(this.dataToBeUnMarshal));
        if(!handleACK(this.dataToBeUnMarshal)){
            clearDataToBeUnMarshal();
            serviceID_receive = 0;
        }
//        else{
//            parse();
//        }
        udpSever.clearRecieveMsg();
        return this.dataToBeUnMarshal;
    }

    public void parse(){
        //this.msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
        //this.serviceID_receive = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 8);
      // System.out.println("[Control]   --parse--   serviceID:   " + this.serviceID_receive);
    }

    public void clearDataToBeUnMarshal() {
        System.out.println("[Control]   -- clearDataToBeUnMarshal-- Clear Data");
        this.dataToBeUnMarshal = new byte[0];
    }


    public boolean handleACK(byte[] receivedData) {
        try {
            int isAck = UnMarshal.unmarshalInteger(receivedData, 0);
            msgID = UnMarshal.unmarshalInteger(receivedData, 4);
            System.err.println("ack: " + isAck);
            //todo: step1: if its nack
            if (isAck == 0) {
                int status = UnMarshal.unmarshalInteger(receivedData, 8);
                if (status == 0) {
                    byte[] preProcess = ProcessedTable.get(msgID);
                    //todo step2: check in the hashtable
                    if (preProcess == null) {
                        System.err.println("Not in hash table");
                        //todo step3: if not found: return nack
                        this.udpSever.UDPsend(new byte[]{0, 0, 0, 0});
                    } else {
                        System.err.println("INN hash table");
                        sendResponse(preProcess);
                        System.err.println("resend: ");
                    }
                }
                    return false;

            }else {
                if(ProcessedTable.get(msgID)!=null){
                    sendResponse(ProcessedTable.get(msgID));
                    return false;
                }else{
                    serviceID_receive = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 8);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return true;
    }

    @Override
    public void sendResponse(byte[] sendData) throws IOException{
        System.err.println("resend:------------ ");
        byte[] ackType = new byte[]{0,0,0,1};
        byte[] status = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, status, sendData);
        System.err.println("resend: " + Arrays.toString(addAck_msg));
        udpSever.UDPsend(addAck_msg);
    }
}
