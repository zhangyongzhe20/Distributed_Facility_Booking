package client.boundary;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String selection;
    Boundary_Factory bf = new Boundary_Factory();
    do {
        displayMain();
        selection = sc.nextLine();
        if (Integer.parseInt(selection) > 0 && Integer.parseInt(selection) < 6) {
            Boundary nextpage = bf.createBoundary(selection);
            nextpage.displayMain();
        }
    } while (!selection.equalsIgnoreCase("6"));
}
    public static void displayMain() {
        System.out.println("Facility Booking System (FBS)");
        System.out.println("===========================================");
        System.out.println("1. Service 1. ");
        System.out.println("2. Service 2. ");
        System.out.println("3. Service 3. ");
        System.out.println("4. Service 4. ");
        System.out.println("5. Service 5. ");
        System.out.println("6. Quit");
        System.out.println("============================================");
        System.out.println("Enter choice : ");
    }
}
