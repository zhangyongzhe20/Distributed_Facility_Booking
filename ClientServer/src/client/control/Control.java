package client.control;
import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import static config.Constants.*;

/**
 * @author Z. YZ
 */
public class Control {
    ArrayList<Object> collectedData;
    ArrayList<Object> header;
    byte[] marShalData;
    byte[] unMarShalData;
    UDPClient udpClient;
    static int numOfResend = 0;
    //TODO: used for demo
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
    public void sendAndReceive(byte[] sendData) throws Exception {
        int timeout = 0;
        do {
            try {
                // simulate this sent message is lost during transmission
                if (Math.random() < REQFRATE) {
                    System.out.println("Simulate Request is lost during transmission");
                } else {
                    udpClient.UDPsend(sendData);
                    //this.unMarShalData = null;
                }
                    // get the unMarShalData
                    this.unMarShalData = udpClient.UDPreceive();
                    if(this.unMarShalData != null) {
                        timeout=0;
                        //check whether is NACK
                        if(!handleACK()){
                            numOfResend++;
                            if(numOfResend >= MAXRESENDS){
                                throw new Exception("reach max resend");
                            }
                            continue;
                        }
                        sendAck(true);
                        return;
                    }
            } catch (SocketTimeoutException e){
                timeout++;
                // STEP1. Send NACK to server, at every timeout
                sendAck(false);
                if (timeout >= MAXTIMEOUTCOUNT){
                    throw new TimeoutException("Exceed max time out");
                }
            }
        }while (true);
    }

    /**
     * Marshaling Ack message and send to server
     * @param b
     * @throws IOException
     */
    protected void sendAck(boolean b) throws IOException {
        ArrayList<Object> ackData = new ArrayList<>();
        ackData.add(ACKMSG);
        // add msg ID
        ackData.add(this.collectedData.get(1));
        if(b){
            ackData.add(ACK);
        }
        else {
            ackData.add(NACK);
        }
        // simulate this sent message is lost during transmission
        if (Math.random() < ACKFRATE) {
            System.out.println("Simulate ACK message is lost during transmission");
        } else {
            this.udpClient.UDPsend(marshalMsg(ackData,true));
        }

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
        if(this.unMarShalData != null) {
            //check oepration status
            int status = UnMarshal.unmarshalInteger(this.unMarShalData, INTEGER_LENGTH);
            if(status == 0){
                throw new Exception(UnMarshal.unmarshalString(this.unMarShalData, 2*INTEGER_LENGTH, this.unMarShalData.length));
            }
            // actual data
            return UnMarshal.unmarshalString(this.unMarShalData, 2*INTEGER_LENGTH, this.unMarShalData.length);
        }
        return null;
    }

    /**
     * Handle Piggybacked ACK from server
     * @return
     */
    public boolean handleACK(){
        int isAck = UnMarshal.unmarshalInteger(this.unMarShalData, 0);
        if(DEMO)
        System.err.println("Receive from server, ack status: " + isAck);
        if(isAck == 0){
            return false;
        }
        return true;
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
        //TODO: Test
        this.unMarShalData = null;
        sendAndReceive(this.marShalData);
        return unMarshal();

    }

    /**
     * RESET unMarShalData varaible
     */
    public void resetUnmarshal() {
        this.unMarShalData = null;
    }
}
