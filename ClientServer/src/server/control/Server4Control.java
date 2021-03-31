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
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @author Z. YZ
 */
public class Server4Control extends ControlFactory{
    private String facilityName;
    private int intervals;

    public Server4Control() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
    }
//    private void updateMonitorTables(String facName) {
//        ArrayList<Member> members = CallBack.MonitorTables.get(this.facilityName);
//        if(members == null){
//            members = new ArrayList<>();
//        }
//            Member newMember = new Member(this.udpSever.getClientIPAddress(), this.udpSever.getClientPort(),
//                    this.intervals, LocalDate.now());
//            members.add(newMember);
//            // add this new member to the monitorTables
//            CallBack.MonitorTables.put(this.facilityName, members);
//    }


    /**
     * Unmarshal service 4 data and update the monitor table
     * @param dataTobeUnmarshal
     */
    public void unMarshal(byte[] dataTobeUnmarshal) {
            this.dataToBeUnMarshal = dataTobeUnmarshal;

            int facilityName_length = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 12);
            String facilityName = UnMarshal.unmarshalString(dataTobeUnmarshal, 16, 16 + facilityName_length);

            int interval = UnMarshal.unmarshalInteger(dataTobeUnmarshal, 20+facilityName_length);

            //update table
        ArrayList<Member> members = CallBack.MonitorTables.get(facilityName);
        if(members == null){
            members = new ArrayList<>();
        }
        Member newMember = new Member(this.udpSever.getClientIPAddress(), this.udpSever.getClientPort(),
                interval, LocalDate.now());
        members.add(newMember);
        // add this new member to the monitorTables
        CallBack.MonitorTables.put(facilityName, members);
    }
}
