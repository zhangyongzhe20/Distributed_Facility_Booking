package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarObj {
    public static String returnDate(int interval) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, interval);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM");
        int i =0;
        StringBuilder Date = new StringBuilder();
        for (Date dt = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), dt = start.getTime()) {
            if(i!=0){
                Date.append(sdf.format(dt));
                Date.append("       ");
            }
            i+=1;
        }
        return Date.toString();
    }
}

