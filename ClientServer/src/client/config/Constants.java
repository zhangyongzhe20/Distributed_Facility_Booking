package client.config;

public class Constants {
    //SERVER Invocation Semantics
    public static final int AT_MOST_ONCE = 0;
    public static final int AT_LEAST_ONCE = 1;

    public static final int APPLIEDSEMANTICS = AT_MOST_ONCE;
    //Application params
    public static final int MAX_BOOKING_HOURS = 2;
    public static final String[] AVAILABLE_FACs = {"LT1", "LT2", "MR1", "MR2"};

    //UDP control params
    public static final int MAXTIMEOUTCOUNT = 3;
    public static final double REQFRATE = 0;
    public static final double ACKFRATE = 0;
    public static final double RESFRATE = 0.5;
    public static final int ACK  = 1;
    public static final int NACK = 0;
    public static final int ACKMSG = 0;
    public static final int DataMSG = 1;
    public static final int INTEGER_LENGTH = 4; //4 bytes
    public static final int MAXRESENDS = 100;

    //UDP CLIENTS
    public static final int PORT = 9876;
    public static final int UDPTIMEOUT = 3000; // 1s timeout
    public static final int UDPBUFFERSIZE = 2*1024;
    public static final String CLIENTNAME = "localhost";
}
