package server.control;

import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Control {
    byte[] dataToBeUnMarshal;
    UDPserver udpSever;

    int msgID;
    int serviceID_receive;


    public int getServiceID_receive() {
        return serviceID_receive;
    }

    public Control() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
    }

    public byte[] receive() throws IOException {
        // get the marshal data
        this.dataToBeUnMarshal = udpSever.UDPreceive();
        udpSever.clearRecieveMsg();
        parse();
        return this.dataToBeUnMarshal;
    }

    public void parse(){
        this.msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,12);
        this.serviceID_receive = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 20);
        System.out.println("serviceID:   " + this.serviceID_receive);
    }

    public void clearDataToBeUnMarshal() {
        System.out.println("clear data to be unmarshal");
        this.dataToBeUnMarshal = new byte[0];
    }
}
