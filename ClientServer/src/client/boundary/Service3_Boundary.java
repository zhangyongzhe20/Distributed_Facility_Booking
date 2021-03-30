package client.boundary;

import client.control.Service3Control;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Service3_Boundary extends Boundary {
    private Service3Control s3C;
    private String response;

    public Service3_Boundary() throws SocketException, UnknownHostException {
        this.s3C = new Service3Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterBookingID();
        enterOffset();

        /**
         * Diff Services contain diff marshalled data
         */
        s3C.marshal();
        try {
            /**
             * integratedProcess: sendAndReceive + handleACK + unmarsall
             */
            response = s3C.integratedProcess();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }
        displayReply();
    }

    @Override
    public void displayReply() {
        if(response!=null){
            System.out.println("Your change is successful: " + "\n"+ response);
        }
    }

    private void enterBookingID() {
        int id = readInputInteger("Enter your previous booking ID: ");
        s3C.setBookingID(id);
    }
    private void enterOffset() {
        int offset = readChangeOffset("enter # of hours that you want to advance or postpone your current booking\n" +
                "(eg. -1: advance 1 hour, 1: postpone by 1 hour):");
        s3C.setOffset(offset);
    }
}
