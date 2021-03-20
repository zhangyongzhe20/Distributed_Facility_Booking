package utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CalculateDate {
    //todo: for testing
    public static void main(String[] args) throws Exception {
        String userInputdate = "2021-03-21";
        System.out.println("index: " + calIndexFromToday(userInputdate));
//    try {
//        System.out.println("for marshalled: " + processDate(8, 3, "2021-03-15", "1111111111"));
//    }catch (Exception e){
//        System.err.println(e.getMessage());
//    }
    }

        public static String calIndexFromToday(String userInputdate){
            try{
                // Converting date to Local date
                LocalDate startDate = LocalDate.now();
                LocalDate endtDate = LocalDate.parse(userInputdate);
                // Range = End date - Start date
                int diff = Period.between(startDate, endtDate).getDays();
                if(diff < 0 || diff > 6){
                    throw new Exception("Please enter the date within a week away from today.");
                }

//                System.out.println("Number of days between the start date : " + userInputdate + " and end date : " + endtDate
//                        + " is  ==> " + diff);
                return String.valueOf(diff);
            }catch (DateTimeParseException e){
                System.err.println("Please, enter correct format in yyyy-mm-dd.");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        }


//    private static String processDate(int startTime, int endTime, String date, String slots) throws Exception {
//        String index = calIndexFromToday(date);
//        if((endTime - startTime) < 0 || (endTime - startTime) > 2){
//            throw new Exception("You can book a facility up to 2 hours");
//        }
//        StringBuilder availSlots = new StringBuilder(slots);
//        for(int i = startTime; i<endTime; i++){
//            availSlots.setCharAt(i-8, '0');
//        }
//        return index.concat(String.valueOf(availSlots));
//    }
}
