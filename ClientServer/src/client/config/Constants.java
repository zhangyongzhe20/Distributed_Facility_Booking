package client.config;

public class Constants {
    //Application params
    public static final int MAX_BOOKING_HOURS = 2;
    public static final String[] AVAILABLE_FACs = {"LT1", "LT2", "MR1", "MR2"};

    //UDP control params
    public static final int MAXTIMEOUTCOUNT = 1;
    public static final float REQFRATE = 1;
    public static final float RESFRATE = 0;
    public static final float ACKFRATE = 0;
    public static final int ACK  = 1;
    public static final int NACK = 0;
    public static final int ACKMSG = 0;
    public static final int DataMSG = 1;
    public static final int INTEGER_LENGTH = 4; //4 bytes
    public static final int MAXRESENDS = 3;

    //UDP CLIENTS
    public static final int PORT = 9876;
    public static final int UDPTIMEOUT = 1000; // 1s timeout
    public static final int UDPBUFFERSIZE = 2*1024;
    public static final String CLIENTNAME = "localhost";
}
