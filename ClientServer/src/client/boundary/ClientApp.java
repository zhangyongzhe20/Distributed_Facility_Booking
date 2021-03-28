package client.boundary;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static client.boundary.Boundary.readInputInteger;

public class ClientApp{
    public static void main(String[] args) throws IOException  {
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
