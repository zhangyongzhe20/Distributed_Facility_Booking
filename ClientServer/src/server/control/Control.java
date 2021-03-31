package server.control;

import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Control {
    byte[] dataToBeUnMarshal;
    UDPserver udpSever;

    int msgID;
    int serviceID_receive;
    int msgType;
    int status;

    public static HashMap<Integer, byte[]> msgIDresponseMap = new HashMap<Integer, byte[]>();

    public int getServiceID_receive() {
        return serviceID_receive;
    }

    public int getMsgID() {
        return msgID;
    }
    public int getMsgType() {
        return msgType;
    }
    public int getStatus() {
        return status;
    }

    public Control() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
    }

    /**
     * Receive marshaled data from Client
     * @return
     * @throws IOException
     */
    public byte[] receive() throws IOException {
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPreceive();
        udpSever.clearRecieveMsg();
        parse();
//        // Check if the request has already been processed
//        if ((msgType == 1) && msgIDresponseMap.containsKey(msgID)){
//            System.err.println("[Control] --receive-- The msg Is processed already"+msgID);
//            return new byte[]{9,9,9,9,(byte) msgID};
//        }
        return this.dataToBeUnMarshal;
    }

    /**
     * Parse the marshaled data to check serviceID
     */
    public void parse(){
        this.msgType = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 0);
        if(this.msgType == 0){
            this.status = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 8);
        }else{
            this.serviceID_receive = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 8);
        }
        this.msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
        //System.out.println("[Control]   --parse--    msgType:   " +this.msgType + " Message ID:  "+this.msgID+" ServiceID: " + this.serviceID_receive);
    }

    /**
     * Clear data
     */
    public void clearDataToBeUnMarshal() {
        //System.out.println("[Control]   -- clearDataToBeUnMarshal-- Clear Data");
        this.dataToBeUnMarshal = new byte[0];
    }

    /**
     * Send Response back to client
     * @param bytes
     * @throws IOException
     */
    public void sendResponse(byte[] bytes) throws IOException {
        udpSever.UDPsend(bytes);
    }

    /**
     * Send NACK back to client
     * @throws IOException
     */
    public void sendNACK() throws IOException {
        udpSever.UDPsend(new byte[] {0,0,0,0});
    }
}
