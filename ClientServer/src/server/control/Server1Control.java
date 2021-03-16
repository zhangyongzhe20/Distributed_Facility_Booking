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

    public Server1Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    public String unMarshal(ArrayList<Facility> facilityArrayList) throws IOException {
        receive();
        if (this.dataToBeUnMarshal.length != 0)
        {
            int realmsg_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            String facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + realmsg_length);
            for (Facility f: facilityArrayList)
            {
                if (f.getFacilityName().equals(facilityName)){
                    int intervals = 7;

                    String days = "";
                    for (int d = 0;d < intervals; d++) {
                        days += "           ";
                        days += (d+1);
                    }
                    this.queryInfo += days;
                    f.setPrintSlot(intervals);
                    this.queryInfo += f.getPrintResult();
                }
            }
            System.out.println("real data: "+this.queryInfo);
            return facilityName;
        }
        return null;
    }

    public void marshal() throws TimeoutException, IOException
    {
        System.out.println("Marshal: "+this.queryInfo);
        this.marshaledData = Marshal.marshalString(this.queryInfo);
        send(this.marshaledData);
    }


}
