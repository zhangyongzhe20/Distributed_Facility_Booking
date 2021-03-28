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

public class Server4Control extends ControlFactory{
    private String facilityName;
    private int intervals;

    public Server4Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        //this.marshaledData = new byte[0];
    }

    public String unMarshal() throws IOException {
            // update Monitor Tables
            updateMonitorTables();

            return new String();
    }

    private void updateMonitorTables() {
//        //TODO: Need to check the request is duplicated or not if uses at-most-semantics
        ArrayList<Member> members = CallBack.MonitorTables.get(this.facilityName);
        if(members == null){
            members = new ArrayList<>();
        }
            Member newMember = new Member(this.udpSever.getClientIPAddress(), this.udpSever.getClientPort(),
                    this.intervals, LocalDate.now());
            members.add(newMember);
            // add this new member to the monitorTables
            CallBack.MonitorTables.put(this.facilityName, members);
    }
}
