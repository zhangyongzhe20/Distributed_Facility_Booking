package client.boundary;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Scanner;

import static client.config.Constants.AVAILABLE_FACs;

public abstract class Boundary {
    static Scanner sc = new Scanner(System.in);
    public abstract void displayMain() throws Exception;
    public abstract void displayReply();
    //todo parsers defined in the blow

    /**
     * Used in Service1
     * @param message
     * @return
     */
    public static int readNumOfQueryDays(String message) {
        int input = -1;
        while (input <= 0) {
            try {
                input = Integer.parseInt(readInputString(message));
                if(input > 7){
                    System.err.println("You only can query the availabilities up to 7 days");
                    input = -1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter in digits");
            }
        }
        return input;
    }

    public static String readInputDate(String message){
        String output = null;
        while(output == null) {
            try {
                // Converting date to Local date
                LocalDate startDate = LocalDate.now();
                LocalDate endtDate = LocalDate.parse(readInputString(message));
                // Range = End date - Start date
                int diff = Period.between(startDate, endtDate).getDays();
                if (diff < 1 || diff > 6) {
                    throw new Exception("Please enter the date from tomorrow onwards and within a week away from today.");
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
                    System.err.println("Please enter in digits");
                }
            }
            return input;
        }


    /**
     * Used to limit the requested facility
     * @param message
     * @return
     */
    public static String readInputFacility(String message) {
        String input = readInputString(message);
        while(!Arrays.stream(AVAILABLE_FACs).anyMatch(input::equals)) {
            System.err.println("Facility is not found");
            input = readInputString(message);
        }
        return input;
    }


    public static int readFacType(String message) {
        int input = readInputInteger(message);
        while (input != 1 && input != 2) {
            try {
                input = readInputInteger(message);
            } catch (NumberFormatException e) {
                System.err.println("Please enter in digits");
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
                System.err.println("Please enter in digits");
            }
        return input;
    }


    public static String readInputString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }


    public static int readInputInteger(String message) {
        int input = -1;
        while (input < 0) {
            try {
                input = Integer.parseInt(readInputString(message));
            } catch (NumberFormatException e) {
                System.err.println("Please enter in digits");
            }
        }
        return input;
    }
}
