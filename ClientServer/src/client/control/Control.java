package client.control;
import utils.UnMarshal;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import static client.config.Constants.*;

/**
 * @author Z. YZ
 */
public class Control {
    ArrayList<Object> collectedData;
    byte[] marShalData;
    byte[] unMarShalData;
    UDPClient udpClient;
    //TODO: Diff client???
    public static int msgID = 0;

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
                if (Math.random() < FRATE) {
                    System.out.println("Simulate this sent message is lost during transmission");
                } else {
                    udpClient.UDPsend(sendData);
                    // get the unMarShalData
                    this.unMarShalData = udpClient.UDPreceive();
                    if(this.unMarShalData != null){
                        sendAck(true);
                        return;
                    }
                }
            } catch (SocketTimeoutException e) {
                timeout++;
                if (timeout >= MAXTIMEOUTCOUNT){
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
        return utils.Marshal.marshalMsgData(collectedMsg, isAck);
    }


    /**
     * Used to unMarshal reply from servers, trim the header
     * @return
     */
    public String unMarshal() throws Exception {
        if(this.unMarShalData.length != 0) {
            //check oepration status
            int status = UnMarshal.unmarshalInteger(this.unMarShalData, INTEGER_LENGTH);
            //todo: remove later
            //status  = 0;
            if(status == 0){
                throw new Exception(UnMarshal.unmarshalString(this.unMarShalData, 2*INTEGER_LENGTH, this.unMarShalData.length));
            }
            // actual data
            return UnMarshal.unmarshalString(this.unMarShalData, 2*INTEGER_LENGTH, this.unMarShalData.length);
        }
        return null;
    }

    public void handleACK() throws Exception {
        int isAck = UnMarshal.unmarshalInteger(this.unMarShalData, INTEGER_LENGTH);
        int numOfResend = 0;
        while(isAck == 0 && numOfResend < MAXRESENDS){
            System.err.println("Server not receive ACK!");
            sendAndReceive(marShalData);
            isAck = UnMarshal.unmarshalInteger(this.unMarShalData, INTEGER_LENGTH);
            //Increase counter
            numOfResend++;
        }
        //TODO REMOVE LATER
        //isAck = 0;
        if(isAck == 0){
            throw new Exception("Reach the max times of resend");
        }
    }

    /**
     * Get an unique ID for marshalling data
     * @return Message ID
     */
    public int getMsgID(){
        return msgID++;
    }

    /**
     * integratedProcess: sendAndReceive + handleACK + unmarsall
     */
    public String integratedProcess() throws Exception {

        sendAndReceive(this.marShalData);

        handleACK();
//        System.err.println("hi");
        return unMarshal();

    }
}
