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
    public void sendAndReceive(byte[] sendData) throws Exception {
        System.err.println(numOfResend);
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
                        //TODO check whether is NACK
                        if(!handleACK()){
                            numOfResend++;
                            if(numOfResend >= MAXRESENDS){
                                throw new Exception("reach max resend");
                            }
                            continue;
                        }
                        sendAck(true);
                        //TODO REMOVE LATER
                        //System.out.println("received: " + Arrays.toString(unMarShalData));
                        return;
                    }
            } catch (SocketTimeoutException e){
                timeout++;
                //TODO: STEP1. Send NACK to server, at every timeout
                sendAck(false);
                if (timeout >= MAXTIMEOUTCOUNT){
                    throw new TimeoutException("Exceed max time out");
                }
            }
        }while (true);
    }

    protected void sendAck(boolean b) throws IOException {
        ArrayList<Object> ackData = new ArrayList<>();
        ackData.add(ACKMSG);
        //TODO: add msg ID
//        System.err.println("msg id in ack: " + this.collectedData.get(1));
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

    public boolean handleACK(){
//        System.err.println(Arrays.toString(this.unMarShalData));
        int isAck = UnMarshal.unmarshalInteger(this.unMarShalData, 0);
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

        sendAndReceive(this.marShalData);
        return unMarshal();

    }


    public static byte[] concat(byte[] a, byte[] b, byte[] c) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        baos.write(c);
        byte[] d = baos.toByteArray();
        return d;
    }

    public static void main(String[] args) throws IOException {
        byte[] ackMsg = new byte[]{0,0,0,0};
        byte[] msgID = new byte[]{0,0,0,0};
        byte[] status = new byte[]{0,0,0,1};
        Control c = new Control();
        c.udpClient.UDPsend(concat(ackMsg,msgID,status));
    }

    public void resetUnmarshal() {
        this.unMarShalData = null;
    }
}
