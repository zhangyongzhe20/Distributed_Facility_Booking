package server.control;

import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Control {
    byte[] dataToBeUnMarshal;
    byte[] marshaledData;
    UDPserver udpSever;

    int msgID;
    int serviceID;
    byte[] ackType;
    int request;

    // Define a hashmap to store MsgID
    HashMap<Integer, byte[]> msgIDMap = new HashMap<>();

    // 1. unmarshal data
    // 2. Check it is ACK or request.
    // If is ACK, end process; If request check messageID is stored in map or not
    // 3. send message

    public Control() throws SocketException, UnknownHostException {
       this.udpSever = UDPserver.getInstance();
    }

    /**
     *
     * @param
     * @return
     */
    public void receive() throws IOException{
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPrecieve();

        int length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0);
        System.out.println(length);
        this.request = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
        System.out.println(this.request);

        int length2 = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,8);
        System.out.println(length2);

        int msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,12);
        System.out.println(msgID);

        int length3 = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16);
        System.out.println(length3);

        this.serviceID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 20);
        System.out.println("serviceID:" + this.serviceID);

    }

    public void send(byte[] sendData) throws IOException {
        if (this.request == 0){
            // Msg Type is ACK
            System.out.println("Received ACK msg");
        }
        else
        {
            System.out.println("Msg Type is request");
            this.msgID = msgID;
            // Use serviceID check whether client request is valid or not
            if (this.serviceID >= 1 && this.serviceID <=4) {
                System.out.println("Service ID"+this.serviceID);

//                // Use msgID to check whether this message is processed or not
//                //todo checking the msgID is only required at at-most-once semantics
//                if (this.msgIDMap.containsKey(this.msgID)) {
//                    //todo: send the value of the msgID back to client
//                } else {
//                    //todo: process the request and update the map
//                } // TODO: What is the logic here?

                // send reply to client with ACK =  1
                this.ackType = new byte[]{0,0,0,1};
                byte[] addAck_msg = concat(ackType, sendData);
                udpSever.UDPsend(addAck_msg);
            }
            else
            {
                // send reply to client with ACK = 0
                this.ackType = new byte[] {0,0,0,0};
                System.out.println("send reply to client with ACK = 0");
                udpSever.UDPsend(ackType);
            }

        }
    }

    public static byte[] concat(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        byte[] c = baos.toByteArray();
        return c;
    }
}
