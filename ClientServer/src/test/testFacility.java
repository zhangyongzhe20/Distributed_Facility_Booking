package test;

import server.FacilityEntity.Facility;

public class testFacility {


    public static void main(String[] args) {
        Facility LT1 = new Facility("LT1");
        Facility LT2 = new Facility("LT2");

        LT1.bookAvailability(7,2);
        LT2.bookAvailability(3, 4);

        String days = "";
        for (int d = 0;d < 7; d++) {
            days += "           ";
            days += (d+1);
        }
        System.out.println(days);


        LT1.printFacility(7);
        System.out.println(LT1.getPrintResult());

        LT2.printFacility(7);
        System.out.println(LT2.getPrintResult());
    }

}
