package client.boundary;

import java.util.Scanner;

public abstract class Boundary {
    Scanner sc = new Scanner(System.in);
    public abstract void displayMain();
    public abstract void displayReply();
    //todo parsers defined in the blow
    public String readInputString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public int readInputInteger(String message) {
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
}
