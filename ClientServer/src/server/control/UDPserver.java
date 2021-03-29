package server.control;

import java.io.IOException;
import java.net.*;

import static client.config.Constants.REQFRATE;
import static client.config.Constants.RESFRATE;

public class UDPserver {
    private DatagramSocket serverSocket;
    private InetAddress clientIPAddress;
    private int serverPort = 9876;
    private int clientPort = 9877;
    private int udptimeout = 2000; //2s timeout

    byte[] recieve_msg = new byte[512];

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
        DatagramPacket request = new DatagramPacket(recieve_msg, recieve_msg.length);
        this.serverSocket.receive(request);
        this.clientIPAddress = request.getAddress();
        this.clientPort = request.getPort();
        //System.out.println("[UDP Server]    --UDPreceive--  Received UDP: " + new String(request.getData()));
        return request.getData();
    }

    public void UDPsend(byte[] message) throws IOException {
        // Send UDP reply to client
        try {
            DatagramPacket reply = new DatagramPacket(message, message.length, this.clientIPAddress, clientPort);
            if (Math.random() < RESFRATE) {
                System.out.println("Simulate Request is lost during transmission");
            } else {
                System.err.println("send reply to book");
                serverSocket.send(reply);
            }

        } catch (IOException e) {
            System.err.println("[UDP Server]    --UDPsend--     Failed to receive/send packet.");
            e.printStackTrace();
        }
    }

    public void clearRecieveMsg() {
        System.out.println("[UDP Server]    --clearRecieveMsg--  clear");
        this.recieve_msg = new byte[512];
    }
    public InetAddress getClientIPAddress() {
        return clientIPAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void UDPMonitorsend(byte[] facilityInfo, InetAddress ipAddress, int port) {
        // Send UDP reply to client
        try {
            DatagramPacket reply = new DatagramPacket(facilityInfo, facilityInfo.length, ipAddress, port);
            System.err.println("send notify");
            serverSocket.send(reply);

        } catch (IOException e) {
            System.err.println("[UDP Server]    --UDPsend--     Failed to receive/send packet.");
            e.printStackTrace();
        }
    }
}
