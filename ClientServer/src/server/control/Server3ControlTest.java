package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.UnMarshal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Server3ControlTest extends ControlTest {
    private int queryBookingID;
    private int offset;

    public Server3ControlTest() throws SocketException, UnknownHostException {
        super();
        this.dataToBeUnMarshal = new byte[0];
        this.marshaledData = new byte[0];
        this.ControlID = 3;
    }

    public String unMarshal(ArrayList<Facility> facilityArrayList, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        receive();
        if (this.dataToBeUnMarshal.length != 0){
            this.queryBookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 28);
            System.out.println("Query Booking ID: "+this.queryBookingID);
            this.offset = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 36);
            System.out.println("Offset: "+this.offset);
        }
        return null;
    }

    public void marshal() throws TimeoutException, IOException {

    }
}
