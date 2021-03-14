package test;

import client.control.Service1Control;
import server.control.Server1Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class testServer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Server1Control server1 = new Server1Control(); // server
        while (true)
        {
            server1.Marshal();
            String data = server1.unMarshal();
            System.out.println(data);
        }
    }
}