package test;

import client.control.Service1Control;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class testClient {
    public static void main(String[] args) throws IOException, TimeoutException {
        Service1Control service1 = new Service1Control();
        while (true)
        {
            service1.marshal();
            String data = service1.unMarshal();
            System.out.println(data);
        }
    }
}
