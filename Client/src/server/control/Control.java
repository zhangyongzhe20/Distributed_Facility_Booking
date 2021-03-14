package server.control;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Control {
    byte[] dataToBeUnMarshal;
    byte[] marshaledData;
    UDPserver udpSever;

    int msgID;
    int serviceID;
    byte[] ackType;

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
     * @param sendData
     * @return
     */
    public void sendAndReceive(byte[] sendData) throws IOException {
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPrecieve();
        if (dataToBeUnMarshal[0] == 0){
            // Msg Type is ACK
            System.out.println("Received ACK msg");
        }
        else
        {
            // Msg Type is request
            this.msgID = this.dataToBeUnMarshal[1];
            this.serviceID = this.dataToBeUnMarshal[2];

            // Use serviceID check whether client request is valid or not
            if (this.serviceID >= 1 && this.serviceID <=5) {

                // Use msgID to check whether this message is processed or not
                if (this.msgIDMap.containsKey(this.msgID))
                {}
                else {} // TODO: What is the logic here?

                // send reply to client with ACK =  1
                this.ackType = new byte[] {1};
                udpSever.UDPsend(concat(ackType, sendData));
            }
            else
            {
                // send reply to client with ACK = 0
                this.ackType = new byte[] {0};
                udpSever.UDPsend(ackType);
            }

        }
    }

    public static byte[] concat(byte[] a, byte[] b) {
        int lenA = a.length;
        int lenB = b.length;
        byte[] c = Arrays.copyOf(a, lenA + lenB);
        System.arraycopy(b, 0, c, lenA, lenB);
        return c;
    }



}
