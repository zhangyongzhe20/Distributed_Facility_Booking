package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.MonthDateParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static server.control.Control.msgIDresponseMap;

public class ControlFactory {
    protected byte[] dataToBeUnMarshal;
    protected byte[] marshaledData;
    protected UDPserver udpSever;
    protected byte[] ackType;
    protected byte[] status;
    protected boolean processed;
    private MonthDateParser dateParser;
    static ArrayList<Facility> facilityArrayList;

    public ControlFactory() throws SocketException, UnknownHostException {
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.dateParser = new MonthDateParser();
        Facility LT1 = new Facility("LT1", 1);
        Facility LT2 = new Facility("LT2", 2);
        Facility MR1 = new Facility("MR1", 3);
        Facility MR2 = new Facility("MR2", 4);
        // Add some user predefined data here
        LT1.bookAvailability(1,2); // 2021-03-30 14-15
        LT1.bookAvailability(1,3); // 2021-03-30 15-16
        LT2.bookAvailability(3, 2); // 2021-03-28 9-10

        facilityArrayList = new ArrayList<>();
        facilityArrayList.add(LT1);
        facilityArrayList.add(LT2);
        facilityArrayList.add(MR1);
        facilityArrayList.add(MR2);
    }


    /**
     *
     * @param dataToBeUnMarshal
     * @param BookingIDArrayList
     * @return
     * @throws IOException
     */
    public String unMarshal(byte[] dataToBeUnMarshal, ArrayList<BookingID> BookingIDArrayList) throws IOException{
        return null;
    }

    /**
     *
     * @throws TimeoutException
     * @throws IOException
     */
    public void marshalAndSend() throws TimeoutException, IOException {}

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     * @throws IOException
     */
    public static byte[] concat(byte[] a, byte[] b, byte[] c) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(a);
        baos.write(b);
        baos.write(c);
        byte[] d = baos.toByteArray();
        return d;
    }

    /**
     *
     * @param sendData
     * @throws IOException
     */
    public void send(byte[] sendData) throws IOException{}

    /**
     *
     * @param sendData
     * @param sendMonitorData
     * @param facilityName
     * @param msgID
     * @throws IOException
     */
    public void send(byte[] sendData, byte[] sendMonitorData, String facilityName, int msgID) throws IOException{
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, this.status, sendData);
        byte[] addAck_msg_monitor = concat(ackType, this.status, sendMonitorData);
        msgIDresponseMap.put(msgID, addAck_msg);
        udpSever.UDPsend(addAck_msg);
        //notify
        CallBack.notify(facilityName, addAck_msg_monitor);
    }

    /**
     *
     * @param interval
     * @param facilityName
     * @return
     */
    String getLatestQueryInfo(int interval, String facilityName)
    {
        String queryInfo = "";
        for (Facility f: this.facilityArrayList)
        {
            if (f.getFacilityName().equals(facilityName)){
                String days = "   ";
                for (int d = 0;d < interval; d++) {
                    days += "            ";
                    days += dateParser.getMonth() + (d+1+dateParser.getDate());
                }
                queryInfo += days;
                f.setPrintSlot(interval);
                queryInfo += f.getPrintResult();
            }
        }
        return queryInfo;
    }
}
