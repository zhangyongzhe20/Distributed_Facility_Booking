package server.control;

import server.FacilityEntity.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * @author Z. YZ
 */
public class CallBack {
    /**
     * Key: facility name
     */
    public static HashMap<String, ArrayList<Member>> MonitorTables = new HashMap<>();


    /**
     * Invoked every time when a service changes the facility bookings
     * @param FacilityName: the facility has its availabilities updated
     * @param facilityInfo: the facility's updated availabilities(in bytes)
     * @throws IOException
     */
    public static void notify(String FacilityName, byte[] facilityInfo) throws IOException {
        ArrayList<Member> members = MonitorTables.get(FacilityName);
        if(members != null){
            for(Member member: members){
                if(member != null || member.isWithIntervals()) {
                    UDPserver.getInstance().UDPMonitorsend(facilityInfo, member.getIpAddress(), member.getPort());
                }
            }
        }
        //MonitorTables.remove(FacilityName);
    }
}
