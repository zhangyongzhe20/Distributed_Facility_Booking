package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MonthDateParser {
    int date;
    String month;
    public MonthDateParser() {
        String DATE_FORMAT = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String date = sdf.format(c1.getTime()); // date = date.substring(0,8) currTime = date.substring(8,12)
        this.month = date.substring(4,6);
        this.date = Integer.parseInt(date.substring(6, 8));
    }

    public int getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }
}
