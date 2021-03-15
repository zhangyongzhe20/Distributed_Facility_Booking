package client.control;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * @author Z. YZ
 */
public class Control {
    ArrayList<Object> collectedData;
    byte[] marShalData;
    byte[] unMarShalData;
    UDPClient udpClient;
    //UDP control params
    public static int msgID = 0;
    public float failureRate = 0;
    public int maxTimeout = 1;
    public int ACK  = 1;
    public int NACK = 0;
    public int ACKMSG = 0;
    public int DataMSG = 1;
    public int INTEGER_LENGTH = 4; //4 bytes

    public Control() throws SocketException, UnknownHostException {
        this.udpClient = UDPClient.getInstance();
    }


    /**
     * UDP: Send and Receive with logic of timeout and failure rate
     * @param sendData
     * @throws TimeoutException
     * @throws IOException
     */
    public void sendAndReceive(byte[] sendData) throws TimeoutException, IOException {
        int timeout = 0;
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
        ackData.add(ACKMSG);
        if(b){
            ackData.add(ACK);
        }
        else {
            ackData.add(NACK);
        }
        this.udpClient.UDPsend(marshalMsg(ackData, true));
    }

    /**
     * Using Marshal class in utils
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @param isAck check if the marshalling data is a Ack msg
     * @return {@code byte[]} the bytes contains data information, send followed by marshalMsgHeader
     */
    public static byte[] marshalMsg(ArrayList<Object> collectedMsg, Boolean isAck){
        //System.out.println("collected Msg in marshalMsg function" + collectedMsg);
        return utils.Marshal.marshalMsgData(collectedMsg, isAck);
    }

    /**
     * Get an unique ID for marshalling data
     * @return Message ID
     */
    public int getMsgID(){
        return msgID++;
    }
}
