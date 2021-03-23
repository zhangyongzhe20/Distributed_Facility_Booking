package server.control;

import server.FacilityEntity.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CallBack {
    public static HashMap<String, ArrayList<Member>> MonitorTables;
    /**
     *
     * @param FacilityName: the facility has its availabilities updated
     * @param facilityInfo: the facility's updated availabilities(in bytes)
     * @throws IOException
     */
    public static void notify(String FacilityName, byte[] facilityInfo) throws IOException {
        UDPserver udpServer;
        ArrayList<Member> members = MonitorTables.get(FacilityName);
        if(!members.isEmpty()){
            for(Member member: members){
                if(member != null || member.isWithIntervals()) {
                    udpServer = new UDPserver(member.getIpAddress(), member.getPort());
                    udpServer.UDPsend(facilityInfo);
                }
            }
        }
        // after notify, clear the lists
        MonitorTables.remove(FacilityName);
    }
}
