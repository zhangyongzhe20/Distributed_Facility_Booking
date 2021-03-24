package client.boundary;

import client.control.Service3Control;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Service3_Boundary extends Boundary {
    private Service3Control s3C;

    public Service3_Boundary() throws SocketException, UnknownHostException {
        this.s3C = new Service3Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterBookingID();
        enterOffset();

        //marshal
        s3C.marshal();
        //handle response and display reply
        displayReply();
    }


    @Override
    public void displayReply() {
        int reply;
        do {
            reply = s3C.unMarshal();
            switch (reply) {
                case -2:
                    System.err.println("This service is not available in server");
                case -1:
                    System.err.println("Your booking ID is not found, please enter a correct one.");
                    break;
                case 0:
                    System.err.println("Your new time slots are not available, please enter another offset.");
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
        s3C.setBookingID(id);
    }
    private void enterOffset() {
        int offset = readChangeOffset("enter # of hours that you want to advance or postpone your current booking\n" +
                "(eg. 1: advance 1 hour, -1: postpone by 1 hour):");
        s3C.setOffset(offset);
    }
}
