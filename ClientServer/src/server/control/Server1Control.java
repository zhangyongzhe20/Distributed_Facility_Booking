package server.control;

import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server1Control {
    private String queryInfo;
    private Boolean successQuery = false;

    private byte[] dataToBeUnMarshal;
    private byte[] marshaledData;
    UDPserver udpSever;
    byte[] ackType;

    public Server1Control() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.queryInfo = "";
    }

    public void clearQueryInfo() {
        this.queryInfo = "";
    }

    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (dataTobeUnmarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 24);
            String facilityName = UnMarshal.unmarshalString(dataTobeUnmarshal, 28, 28 + facilityName_length);

            int interval = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 32+facilityName_length);
            this.queryInfo = getQueryInfo(facilityArrayList, interval, facilityName);
            System.out.println("queryinfo: "+queryInfo);
            return facilityName;
        }
        return null;
    }

    public void marshalAndSend() throws TimeoutException, IOException
    {

        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4) == 0){
            // Msg Type is ACK
            System.out.println("Received ACK msg");
        }
        else {
            System.out.println("Msg Type is request");
            if (this.successQuery)
            {
                this.marshaledData = Marshal.marshalString(this.queryInfo);
                send(this.marshaledData);
            }else
            {
                this.marshaledData = Marshal.marshalString("There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
                send(this.marshaledData);
            }
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    String getQueryInfo(ArrayList<Facility> facilityArrayList, int interval, String facilityName)
    {
        for (Facility f: facilityArrayList)
        {
            if (f.getFacilityName().equals(facilityName)){
                this.successQuery = true;
                String days = "";
                for (int d = 0;d < interval; d++) {
                    days += "           ";
                    days += (d+1);
                }
                queryInfo += days;
                f.setPrintSlot(interval);
                queryInfo += f.getPrintResult();
            }
        }
        return queryInfo;
    }

    public void send(byte[] sendData) throws IOException{
        System.out.println("Success query: "+this.successQuery);
        if (this.successQuery){
            this.ackType = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, sendData);
            udpSever.UDPsend(addAck_msg);
        }
        else {
                this.ackType = new byte[] {0,0,0,0};
                System.out.println("send reply to client with ACK = 0");
                udpSever.UDPsend(ackType);
        }
    }

    public static byte[] concat(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        byte[] c = baos.toByteArray();
        return c;
    }

}
