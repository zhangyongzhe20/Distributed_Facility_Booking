package client.control;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Control {
    ArrayList<Object> collectedData;
    byte[] marShalData;
    byte[] unMarShalData;
    UDPClient udpClient;
    //UDP control params
    private static int msgID = 0;
    private float failureRate = 0;
    private int maxTimeout = 1;

    public Control() throws SocketException, UnknownHostException {
        this.udpClient = UDPClient.getInstance();
    }

    /**
     *
     * @param sendData
     * @return
     */
    public void sendAndReceive(byte[] sendData) throws TimeoutException, IOException {
        int timeout = 0;
        byte[] response = new byte[0];
        do {
            try {
                // simulate this sent message is lost during transmission
                if (Math.random() < this.failureRate) {
                    System.out.println("Simulate this sent message is lost during transmission");
                } else {
                    udpClient.UDPsend(sendData);
                    // get the unMarShalData
                    this.unMarShalData = udpClient.UDPreceive();
                    if(this.unMarShalData != null){
                        sendAck(true);
                    }
                }
            } catch (SocketTimeoutException e) {
                timeout++;
                if (timeout >= this.maxTimeout){
                    sendAck(false);
                    throw new TimeoutException("Exceed max time out");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (true);
    }

    protected void sendAck(boolean b) throws IOException {
        ArrayList<Object> ackData = new ArrayList<>();
        ackData.add(0);
        if(b){
            ackData.add(1);
        }
        else {
            ackData.add(0);
        }
        this.udpClient.UDPsend(marshalMsg(ackData, true));
    }

    /**
     * Using Marshal class in Utils
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @return {@code byte[]} the bytes contains data information, send followed by marshalMsgHeader
     */
    public static byte[] marshalMsg(ArrayList<Object> collectedMsg, Boolean isAck){
        System.out.println("collected Msg in marshalMsg function" + collectedMsg);
        return Utils.Marshal.marshalMsgData(collectedMsg, isAck);
    }

    /**
     *
     * @return Message ID
     */
    public int getMsgID(){
        return msgID++;
    }
}
