package server.FacilityEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingID {
    private String bookingInfoString;
    private int ID;
    private boolean cancel;

    public boolean isCancel() {
        return cancel;
    }

    public BookingID(int id, int day, String facilityName, int StartTime, int EndTime) {
        String DATE_FORMAT = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String date = sdf.format(c1.getTime()); // date = date.substring(0,8) currTime = date.substring(8,12)
        this.ID = id;
        this.cancel = false;
        this.bookingInfoString = getIDString() + "-"+Integer.toString(day + Integer.parseInt(date.substring(0,8)))+"-"
                +facilityName+"-"+convertIntToString(StartTime)+convertIntToString(EndTime)+"-"+date.substring(8,12);
    }

    public String getBookingInfoString() {
        return bookingInfoString;
    }

    public int getID(){
        return ID;
    }

    public String getIDString() {
        String IDString = "0";
        System.out.println("Get ID String");
        if (this.ID<10){
            IDString += Integer.toString(this.ID);
            return IDString;
        }else{
            return Integer.toString(this.ID);
        }
    }

    public void cancelBooking(){
        this.cancel = true;
    }

    public static String convertIntToString(int time){
        if (time<10){
            System.out.println("[BookingID] --convertIntToString-- original time"+(time));
            return ("0" + Integer.toString(time));
        }else
            System.out.println("[BookingID] --convertIntToString-- original time"+(time));
            return Integer.toString(time);
    }
}
