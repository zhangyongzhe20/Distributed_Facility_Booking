package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPserver {
    static DatagramSocket aSocket = null;
    static int PORT = 9876;

    public static void main(String[] args) {
        // ---------------------- 1. Open UDP Socket at well-known port ----------------------
        try {
            aSocket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.err.println("Failed to create Datagram Socket.");
            e.printStackTrace();
        }

        while (true)
        {
            System.out.println("Wait for message...");
            try {
                // ---------------------- 2. Listen for UDP request from client ----------------------
                byte[] recieve_msg = new byte[512];
                DatagramPacket request = new DatagramPacket(recieve_msg, recieve_msg.length);
                aSocket.receive(request); // TODO: Change to unmarshal

                String output = new String(request.getData());
                System.out.println("Received: " + output);

                InetAddress IPAddress = request.getAddress();
                int port = request.getPort();
                byte[] send_msg = output.getBytes();

                // ---------------------- 3. Send UDP reply to client ----------------------
                DatagramPacket reply = new DatagramPacket(send_msg, send_msg.length, IPAddress, port);
                aSocket.send(reply);
            } catch (IOException e) {
                System.err.println("Failed to receive/send packet.");
                e.printStackTrace();
            }
        }
    }

}
