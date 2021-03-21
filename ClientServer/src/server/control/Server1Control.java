package server.control;

import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server1Control extends Control{
    private String queryInfo = "";
    private Boolean successQuery = false;

    public Server1Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.id = 1;
    }

    public String unMarshal(ArrayList<Facility> facilityArrayList) throws IOException {
        receive();
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            String facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + facilityName_length);

            int interval = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 32+facilityName_length);
            this.queryInfo = getQueryInfo(facilityArrayList, interval, facilityName);

            return facilityName;
        }
        return null;
    }

    public void marshal() throws TimeoutException, IOException
    {
        System.out.println("Marshal: "+this.queryInfo);
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

    String getQueryInfo(ArrayList<Facility> facilityArrayList, int interval, String facilityName)
    {
        String queryInfo = "";
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

}
