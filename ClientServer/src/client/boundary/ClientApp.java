package client.boundary;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static client.boundary.Boundary.readInputInteger;
import static config.Constants.*;

public class ClientApp{
    public static void main(String[] args) throws IOException  {
        System.out.println("The default settings:\n" +
                "Request Failure Rate: 0\n" +
                "Acknowledgement Failure Rate: 0\n" +
                "UDP timeout (in second): 5\n" +
                "Max counts of UDP timeout: 10\n" +
                "Max counts of resend requests: 100");
        System.out.println("You can change by: java ClientApp <Request Failure Rate> <Acknowledgement Failure Rate> <UDP timeout (in second)> <Max counts of UDP timeout> <Max counts of resend requests>");
        if (args.length >= 1) {
            REQFRATE = Double.parseDouble(args[0]);
            System.out.println("The simulated request failure rate: " + REQFRATE);
        }
        if (args.length >= 2) {
            ACKFRATE = Double.parseDouble(args[1]);
            System.out.println("The simulated Ack failure rate: " + ACKFRATE);
        }
        if (args.length >= 3) {
            TIMEOUT = Integer.parseInt(args[2]);
            System.out.println("UDP timeout (in second): " + TIMEOUT);
        }
        if (args.length >= 4) {
            MAXTIMEOUTCOUNT = Integer.parseInt(args[3]);
            System.out.println("Max counts of UDP timeout: " + MAXTIMEOUTCOUNT);
        }
        if (args.length >= 5) {
            MAXRESENDS = Integer.parseInt(args[4]);
            System.out.println("Max counts of resend requests: " + MAXRESENDS);
        }
        System.out.println("============================================================================================================================================================================\n");
    Scanner sc = new Scanner(System.in);
    int selection;
    Boundary_Factory bf = new Boundary_Factory();
        do {
            selection = readInputInteger(displayMain());
            if (selection <= bf.getNumOfBoundaries()) {
                Boundary nextpage = bf.createBoundary(selection);
                try {
                    nextpage.displayMain();
                } catch (TimeoutException | IOException te){
                    System.err.println(te.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isContiune();
            }
        } while (selection!=7);
}

    private static String displayMain() {
        String mainPage = "\tFacility Booking System (FBS)\n" +
        "===========================================\n" +
        "1. Query the availability of a facility.\n" +
        "2. Book a facility.\n" +
        "3. Change booking time.\n" +
        "4. Monitor a facility. \n" +
        "5. Auto booking facility(Non-idempotent).\n" +
        "6. Cancel Booking (Idempotent).\n" +
        "7. Quit\n" +
        "============================================\n" +
        "Enter choice : ";
        return mainPage;
    }


    private static void isContiune() {
        String isContinue = Boundary.readInputString("Do you want to continue?");
        if(isContinue.toLowerCase().startsWith("n")){
            System.exit(1);
        }
    }
}
