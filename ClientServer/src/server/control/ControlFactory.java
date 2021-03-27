package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ControlFactory {
    protected byte[] dataToBeUnMarshal;
    protected byte[] marshaledData;
    protected UDPserver udpSever;
    protected byte[] ackType;
    protected byte[] status;

    public ControlFactory() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    //Server1
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList) throws IOException {
        return null;
    }
    // Server2
    public String unMarshal(byte[] dataToBeUnMarshal, ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException{
        return null;
    }

    public void marshalAndSend() throws TimeoutException, IOException {}


    public static byte[] concat(byte[] a, byte[] b, byte[] c) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        baos.write(c);
        byte[] d = baos.toByteArray();
        return d;
    }

    public void send(byte[] sendData) throws IOException{}
}
