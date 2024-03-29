package server.control;

import server.FacilityEntity.Facility;
import utils.CalendarObj;
import utils.Marshal;
import utils.UnMarshal;
import utils.MonthDateParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static server.control.Control.msgIDresponseMap;

public class Server1Control extends ControlFactory{

    private Boolean facilityExist = false;
    private MonthDateParser dateParser;
    protected String queryInfo;


    public Server1Control() throws SocketException, UnknownHostException {
        super();
        this.udpSever = UDPserver.getInstance();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.queryInfo = "";
        this.dateParser = new MonthDateParser();
        this.processed = false;
    }

    public void clearQueryInfo() {
        this.queryInfo = "";
    }

    /**
     * Unmarshal Msg received from server
     * @param dataTobeUnmarshal
     * @return null
     * @throws IOException
     */

    public String unMarshal(byte[] dataTobeUnmarshal) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;

        if (Arrays.equals(Arrays.copyOfRange(this.dataToBeUnMarshal,0,4), new byte[]{9, 9, 9, 9})){
            System.err.println("[Server1]   --unMarshal--   The message has already been processed.");
            this.processed = true;
        }
        if (dataTobeUnmarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 12);
            String facilityName = UnMarshal.unmarshalString(dataTobeUnmarshal, 16, 16 + facilityName_length);

            int interval = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 20+facilityName_length);
            this.queryInfo = getQueryInfo(facilityArrayList, interval, facilityName);
            System.out.println("[Server1]   --unMarshal--   queryInfo: "+queryInfo);
            return facilityName;
        }
        return null;
    }

    /**
     * Send Marshaled Data to Client
     * @throws TimeoutException
     * @throws IOException
     */
    public void marshalAndSend() throws TimeoutException, IOException
    {
        int msgID;
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0)==0){
            // Msg Type is ACK
            System.err.println("[Server1]   --marshalAndSend--  Received ACK msg");
        }
//        else if (this.processed){
//            // MsgID is in table. It has been processed already.
//            System.err.println("[Server1]   --marshalAndSend--   The message has already been processed. Extract from table.");
//            msgID = Arrays.copyOfRange(this.dataToBeUnMarshal,4,5)[0];
//            System.out.println("MSG ID: "+msgID);
//            send(msgIDresponseMap.get(msgID));
//        }
        else {
            System.err.println("[Server1]   --marshalAndSend--  Msg Type is request");
            msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
            if (this.facilityExist)
            {
                this.marshaledData = Marshal.marshalString(this.queryInfo);
                this.status = new byte[]{0,0,0,1};
                send(this.marshaledData, status, msgID);
                this.facilityExist = false;
            }else
            {
                System.out.println("[Server1]   --marshalAndSend--  There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
                this.marshaledData = Marshal.marshalString("There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
                this.status = new byte[]{0,0,0,0};
                send(this.marshaledData, status, msgID);
            }
            //System.err.println("[Server1]   --marshalAndSend-- Add msgID : "+msgID+" to the table.");
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    /**
     *
     * @param facilityArrayList
     * @param interval
     * @param facilityName
     * @return query Information in timeslots
     */
    String getQueryInfo(ArrayList<Facility> facilityArrayList, int interval, String facilityName)
    {
        for (Facility f: facilityArrayList)
        {
            if (f.getFacilityName().equals(facilityName)){
                this.facilityExist = true;
                String days = "             "+ CalendarObj.returnDate(interval);
                queryInfo += days;
                f.setPrintSlot(interval);
                queryInfo += f.getPrintResult();
            }
        }
        return queryInfo;
    }

    /**
     *
     * @param sendData
     * @param status
     * @param msgID
     * @throws IOException
     */
    public void send(byte[] sendData, byte[] status, int msgID) throws IOException{
        System.out.println("[Server1]   --send--    Success query: "+this.facilityExist);
            this.ackType = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, status, sendData);
            udpSever.UDPsend(addAck_msg);
            //update table
            msgIDresponseMap.put(msgID, addAck_msg);
    }

}

