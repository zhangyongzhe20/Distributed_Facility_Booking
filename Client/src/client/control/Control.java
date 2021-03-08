package client.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public abstract class Control {
    ArrayList<Object> collectedData;
    byte[] marShalData;
    byte[] unMarShalData;

    //UDP client
    static DatagramSocket aSocket;
    static int PORT = 9876;
    private static int msgID = 0;

    public abstract void sendAndReceive();
    public abstract void marshal();
    public abstract void unMarshal();


    /**
     *
     * @param sendData
     * @return
     */
    public byte[] sendAndReceive(byte[] sendData, Boolean isAck){
        // ---------------------- 1. Open UDP Socket ----------------------
        try {
            aSocket = new DatagramSocket();
        }// end open socket
        catch (SocketException e)
        {
            System.err.println("Failed to create Datagram Socket.");
            e.printStackTrace();
        }// end catch (SocketException e)

        // ---------------------- 2. Send UDP request to server ----------------------
        try {
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(sendData, sendData.length, aHost, PORT);
            aSocket.send(request);

            if(!isAck) {
                // ---------------------- 3. Receive UDP reply from server
                byte[] receive_msg = new byte[1024];
                DatagramPacket reply = new DatagramPacket(receive_msg, receive_msg.length);
                aSocket.receive(reply);
                System.out.println("Reply: " + new String(reply.getData()));
                //todo: need to implement timeout
                return reply.getData();
            }
        } // end try send request and receive
        catch (IOException e) {
            System.err.println("Failed to receive/send packet.");
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * Using Marshal class in Utils
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @return {@code byte[]} the bytes contains data information, send followed by marshalMsgHeader
     */
    public static byte[] marshalMsg(ArrayList<Object> collectedMsg){
        return Utils.Marshal.marshalMsgData(collectedMsg);
    }

    /**
     *
     * @return Message ID
     */
    public int getMsgID(){
        return msgID++;
    }
}
