package client.control;

import Utils.UnMarshal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClient {
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private int port = 9876;
    private int udptimeout = 2000; // 2s timeout

    private static UDPClient SINGLE_INSTANCE;

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
        this.clientSocket.setSoTimeout(udptimeout);
    }

    public void UDPsend(byte[] message) throws IOException {
        // ---------------------- Send UDP request to server ----------------------
        try {
            DatagramPacket request = new DatagramPacket(message, message.length, this.IPAddress, port);
            //todo remove
            //System.out.println("Send msg to server: " + UnMarshal.unmarshalInteger(message,0));
            this.clientSocket.send(request);
        } catch (IOException e) {
            System.err.println("Failed to receive/send packet.");
            e.printStackTrace();
        }
    }

    public byte[] UDPreceive() throws IOException {
        // ---------------------- Receive UDP reply from server
        byte[] receive_msg = new byte[1024];
        DatagramPacket reply = new DatagramPacket(receive_msg, receive_msg.length);
        this.clientSocket.receive(reply);
        System.out.println("Reply: " + new String(reply.getData()));
        return reply.getData();
    }
}
