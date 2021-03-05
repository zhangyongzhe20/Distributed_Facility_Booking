package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
    static DatagramSocket aSocket = null;
    static int PORT = 9876;

    public static void main(String[] args) throws SocketException {
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
                BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
                String input = inputFromUser.readLine();
                byte[] send_msg = input.getBytes();

                InetAddress aHost = InetAddress.getByName("localhost");
                DatagramPacket request = new DatagramPacket(send_msg, send_msg.length, aHost, PORT);

                aSocket.send(request);


                // ---------------------- 3. Receive UDP reply from server
                byte[] receive_msg = new byte[1024];
                DatagramPacket reply = new DatagramPacket(receive_msg, receive_msg.length);
                aSocket.receive(reply);
                System.out.println("Reply: "+new String(reply.getData()));
            } // end try send request and receive
        catch (IOException e) {
                System.err.println("Failed to receive/send packet.");
                e.printStackTrace();
        }// catch (IOException e)


    }// end psvm
}// end UDPClient
