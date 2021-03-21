package server.FacilityEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingID {
    private String bookingInfoString;
    private int ID;
    private boolean cancel;

    public BookingID(int id, int day, int facilityID, int StartTime, int EndTime) {
        String DATE_FORMAT = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String date = sdf.format(c1.getTime()); // date = date.substring(0,8) currTime = date.substring(8,12)
        this.bookingInfoString = Integer.toString(day + Integer.parseInt(date.substring(0,8)))
                +Integer.toString(facilityID)+Integer.toString(StartTime)+Integer.toString(EndTime)+date.substring(8,12);
        this.ID = id;
        this.cancel = false;
    }

    public String getBookingInfoString() {
        return bookingInfoString;
    }

    public int getID() {
        return ID;
    }
}