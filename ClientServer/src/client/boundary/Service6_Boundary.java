package client.boundary;

import client.control.Service3Control;
import client.control.Service6Control;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Service6_Boundary extends Boundary {
    private Service6Control s6C;

    public Service6_Boundary() throws SocketException, UnknownHostException {
        this.s6C = new Service6Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterBookingID();

        //marshal
        s6C.marshal();
        //handle response and display reply
        displayReply();
    }


    @Override
    public void displayReply() {
        int reply;
        do {
            reply = s6C.unMarshal();
            switch (reply) {
                case -2:
                    System.err.println("This service is not available in server");
                case -1:
                    System.err.println("Your booking ID is not found, please enter a correct one.");
                    break;
                case 1:
                    System.out.println("Your change is successfully!");
                    break;
                default:
                    System.err.println("Wrong reply from server.");
                }
        }while(reply == -1 || reply == 0);  // ask users re-enter
    }

    private void enterBookingID() {
        int id = readInputInteger("Enter your previous booking ID: ");
        s6C.setBookingID(id);
    }
}
