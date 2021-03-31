package server.FacilityEntity;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDate;

/**
 * @author Z. YZ
 */
public class Member {
    private InetAddress ipAddress;
    private int port;
    // the monitoring duration from Client Side
    private int intervals;
    // the starting time of monitor, start from when the server receives the request
    private LocalDate start;

    public Member(InetAddress ipAddress, int port, int intervals, LocalDate start) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.intervals = intervals;
        this.start = start;
    }

    private int getDuration(){
        Duration duration = Duration.between(LocalDate.now(), start);
        //return difference in seconds
         return (int)duration.toSeconds();
    }

    public boolean isWithIntervals(){
        return intervals > getDuration();
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
