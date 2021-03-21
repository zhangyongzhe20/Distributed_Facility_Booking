package server.control;

import server.FacilityEntity.Facility;
import server.FacilityEntity.Member;
import utils.Marshal;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server4Control extends Control{
    private String facilityName;
    private int intervals;

    public Server4Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
    }

    public String unMarshal() throws IOException {
        receive();
        if (this.dataToBeUnMarshal.length != 0)
        {
            int facilityName_length = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            this.facilityName = UnMarshal.unmarshalString(this.dataToBeUnMarshal, 28, 28 + facilityName_length);

            this.intervals = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 32+facilityName_length);
            // update Monitor Tables
            updateMonitorTables();
        }
        return null;
    }

    private void updateMonitorTables(){
        //TODO: Need to check the request is duplicated or not if uses at-most-semantics
        ArrayList<Member> members = CallBack.MonitorTables.get(this.facilityName);
        if(members == null){
            members = new ArrayList<>();
        }
            Member newMember = new Member(this.udpSever.getClientIPAddress(), this.udpSever.getClientPort(),
                    this.intervals, LocalDate.now());
            members.add(newMember);
            CallBack.MonitorTables.put(this.facilityName, members);
    }
}
