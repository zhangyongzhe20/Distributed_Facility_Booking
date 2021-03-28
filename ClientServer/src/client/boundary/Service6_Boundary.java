package client.boundary;

import client.control.Service3Control;
import client.control.Service6Control;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Service6_Boundary extends Boundary {
    private Service6Control s6C;
    private String response;

    public Service6_Boundary() throws SocketException, UnknownHostException {
        this.s6C = new Service6Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterBookingID();

        /**
         * Diff Services contain diff marshalled data
         */
        s6C.marshal();
        try {
            /**
             * integratedProcess: sendAndReceive + handleACK + unmarsall
             */
            response = s6C.integratedProcess();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }
        displayReply();
    }

    @Override
    public void displayReply() {
        if(response!=null){
            System.out.println(response);
        }
    }

    private void enterBookingID() {
        int id = readInputInteger("Enter your previous booking ID: ");
        s6C.setBookingID(id);
    }
}
