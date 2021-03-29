package server.control;

import server.FacilityEntity.Facility;
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

public class Server1Control extends ControlFactory{
    private String queryInfo;
    private Boolean facilityExist = false;
    private MonthDateParser dateParser;


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

    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<Facility> facilityArrayList) throws IOException {
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
            if (this.facilityExist)
            {
                this.marshaledData = Marshal.marshalString(this.queryInfo);
                send(this.marshaledData);
                this.facilityExist = false;
            }else
            {
                System.out.println("[Server1]   --marshalAndSend--  There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
                this.marshaledData = Marshal.marshalString("There is no such Facility. Pls choose in LT1, LT2, MR1, MR2");
                send(this.marshaledData);
            }
            msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
            System.err.println("[Server1]   --marshalAndSend-- Add msgID : "+msgID+" to the table.");
            msgIDresponseMap.put(msgID, marshaledData);
        }
        this.dataToBeUnMarshal = new byte[0];
    }

    String getQueryInfo(ArrayList<Facility> facilityArrayList, int interval, String facilityName)
    {
        for (Facility f: facilityArrayList)
        {
            if (f.getFacilityName().equals(facilityName)){
                this.facilityExist = true;
                String days = "   ";
                for (int d = 0;d < interval; d++) {
                    days += "            ";
                    days += this.dateParser.getMonth() + (d+1+dateParser.getDate());
                }
                queryInfo += days;
                f.setPrintSlot(interval);
                queryInfo += f.getPrintResult();
            }
        }
        return queryInfo;
    }

    public void send(byte[] sendData) throws IOException{
        System.out.println("[Server1]   --send--    Success query: "+this.facilityExist);
            this.ackType = new byte[]{0,0,0,1};
            this.status = new byte[]{0,0,0,1};
            byte[] addAck_msg = concat(ackType, this.status, sendData);
            udpSever.UDPsend(addAck_msg);
    }

}

