package config;

public class Constants {
    //SERVER Params
    public static final int AT_MOST_ONCE = 0;
    public static final int AT_LEAST_ONCE = 1;
    public static int APPLIEDSEMANTICS = AT_MOST_ONCE;
    public static double RESFRATE = 0;

    //CLINET Params
    public static double REQFRATE = 0;
    public static double ACKFRATE = 0;
    public static int TIMEOUT = 5; // 3s timeout
    public static int MAXTIMEOUTCOUNT = 10;
    public static int MAXRESENDS = 100;

    //Application params
    public static final int MAX_BOOKING_HOURS = 2;
    public static final String[] AVAILABLE_FACs = {"LT1", "LT2", "MR1", "MR2"};

    //UDP control params
    public static final int ACK  = 1;
    public static final int NACK = 0;
    public static final int ACKMSG = 0;
    public static final int DataMSG = 1;
    public static final int INTEGER_LENGTH = 4; //4 bytes

    //UDP CLIENTS
    public static final int PORT = 9876;
    public static int UDPTIMEOUT = 1000*TIMEOUT; // 3s timeout
    public static final int UDPBUFFERSIZE = 2*1024;
    public static final String CLIENTNAME = "localhost";  //ip address
}
