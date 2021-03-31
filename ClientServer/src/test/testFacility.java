package test;

import server.FacilityEntity.Facility;
import utils.MonthDateParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class testFacility {


    public static void main(String[] args) {
        Facility LT1 = new Facility("LT1", 1,1);
        Facility LT2 = new Facility("LT2", 2,1);

        LT1.bookAvailability(7,2);
        LT2.bookAvailability(3, 4);

        MonthDateParser dateParser = new MonthDateParser();

        String days = "   ";
        for (int d = 0;d < 7; d++) {
            days += "            ";
            days += dateParser.getMonth() + (d+1+dateParser.getDate());
        }
        System.out.println(days);


        LT1.setPrintSlot(7);
        System.out.println(LT1.getPrintResult());

        LT2.setPrintSlot(7);
        System.out.println(LT2.getPrintResult());
    }


}
