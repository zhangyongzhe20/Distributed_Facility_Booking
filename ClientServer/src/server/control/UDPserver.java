package server.control;

import java.io.IOException;
import java.net.*;

public class UDPserver {
    private DatagramSocket serverSocket;
    private InetAddress clientIPAddress;
    private int serverPort = 9876;
    private int clientPort = 9877;
    private int udptimeout = 2000; //2s timeout

    public static UDPserver SINGLE_INSTANCE;
    public static UDPserver getInstance() throws UnknownHostException, SocketException {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new UDPserver();
        }
        return SINGLE_INSTANCE;
    }

    public UDPserver() throws UnknownHostException, SocketException{
        // Open UDP Socket at well-known port
       this.serverSocket = new DatagramSocket(serverPort);
    }

    public UDPserver(InetAddress ipaddress, int port) throws SocketException {
        this.serverSocket = new DatagramSocket(serverPort);
        this.clientIPAddress = ipaddress;
        this.clientPort = port;
    }

    public byte[] UDPreceive() throws IOException{
        // Listen for UDP request from client
        byte[] recieve_msg = new byte[512];
        DatagramPacket request = new DatagramPacket(recieve_msg, recieve_msg.length);
        this.serverSocket.receive(request);
        this.clientIPAddress = request.getAddress();
        this.clientPort = request.getPort();
        System.out.println("Received: " + new String(request.getData()));
        return request.getData();
    }

    public void UDPsend(byte[] message) throws IOException {
        // Send UDP reply to client
        try {
            DatagramPacket reply = new DatagramPacket(message, message.length, this.clientIPAddress, clientPort);
            serverSocket.send(reply);
        } catch (IOException e) {
            System.err.println("Failed to receive/send packet.");
            e.printStackTrace();
        }
    }

    public InetAddress getClientIPAddress() {
        return clientIPAddress;
    }

    public int getClientPort() {
        return clientPort;
    }
}
