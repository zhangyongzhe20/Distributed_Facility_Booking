package client.boundary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public abstract class Boundary {
    static Scanner sc = new Scanner(System.in);
    public abstract void displayMain() throws Exception;
    public abstract void displayReply();
    //todo parsers defined in the blow

    public static String readInputDate(String message){
        String output = null;
        while(output == null) {
            try {
                // Converting date to Local date
                LocalDate startDate = LocalDate.now();
                LocalDate endtDate = LocalDate.parse(readInputString(message));
                // Range = End date - Start date
                int diff = Period.between(startDate, endtDate).getDays();
                if (diff < 0 || diff > 6) {
                    throw new Exception("Please enter the date within a week away from today.");
                }
                output = String.valueOf(diff);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return output;
        }

        public static int readInputTime(String message){
            int input = -1;
            while (input < 0) {
                try {
                    input = Integer.parseInt(readInputString(message));
                    if(input < 8 || input > 18){
                        System.err.println("Facility is only available between 8am and 6pm");
                        input = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter in digits");
                }
            }
            return input;
        }

        public static String readInputString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    /**
     * Used to limit the requested facility
     * @param message
     * @return
     */
    public static String readInputFacility(String message) {
        String[] availableFac ={"LT1", "LT2", "MR1", "MR2"};
        String input = readInputString(message);
        while(!Arrays.stream(availableFac).anyMatch(input::equals)) {
            System.err.println("Facility is not found");
            input = readInputString(message);
        }
        return input;
    }

    public static int readInputInteger(String message) {
        int input = -1;
        while (input < 0) {
            try {
                input = Integer.parseInt(readInputString(message));
            } catch (NumberFormatException e) {
                System.out.println("Please enter in digits");
            }
        }
        return input;
    }

    /**
     * Used in service3
     * @param message
     * @return
     */
    public static int readChangeOffset(String message) {
        int input = -1;
            try {
                input = Integer.parseInt(readInputString(message));
            } catch (NumberFormatException e) {
                System.out.println("Please enter in digits");
            }
        return input;
    }
}
