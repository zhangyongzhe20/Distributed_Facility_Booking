package client.control;

import java.io.IOException;
import java.net.*;

/**
 * @author zyz
 */
public class UDPClient {
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private int PORT = 9876;
    private int UDPTIMEOUT = 1000; // 1s timeout
    private int UDPBUFFERSIZE = 2*1024;
    private static UDPClient SINGLE_INSTANCE;

    /**
     * To make sure only one UDP Client for transmission is created during services
     * @return the UDP client
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static UDPClient getInstance() throws UnknownHostException, SocketException {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new UDPClient();
        }
        return SINGLE_INSTANCE;
    }

    public UDPClient() throws UnknownHostException, SocketException {
        this.IPAddress = InetAddress.getByName("localhost");
        // ---------------------- 1. Open UDP Socket ----------------------
        this.clientSocket = new DatagramSocket();
        this.clientSocket.setSoTimeout(UDPTIMEOUT);
    }

    /**
     * Naive function of UDP send
     * @param message
     * @throws IOException
     */
    public void UDPsend(byte[] message) throws IOException {
        // ---------------------- Send UDP request to server ----------------------
        try {
            DatagramPacket request = new DatagramPacket(message, message.length, this.IPAddress, PORT);
            this.clientSocket.send(request);
        } catch (IOException e) {
            System.err.println("Failed to receive/send packet.");
            e.printStackTrace();
        }
    }

    /**
     * Naive function of UDP receive
     * @return
     * @throws IOException
     */
    public byte[] UDPreceive() throws IOException {
        // ---------------------- Receive UDP reply from server
        byte[] receive_msg = new byte[UDPBUFFERSIZE];
        DatagramPacket reply = new DatagramPacket(receive_msg, receive_msg.length);
        this.clientSocket.receive(reply);
//        System.out.println("Reply: " + new String(reply.getData()));
        return reply.getData();
    }
}
