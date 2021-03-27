package client.boundary;

import client.control.Service5Control;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Service5_Boundary extends Boundary {
    private final Service5Control s5C;

    public Service5_Boundary() throws SocketException, UnknownHostException {
        this.s5C = new Service5Control();
    }
    @Override
    public void displayMain() throws Exception {
        enterFacType();
        //marshall
        s5C.marshal();
        //handle response and display reply
        displayReply();
    }

    @Override
    public void displayReply() {
        System.out.println("Your booking ID: " + s5C.unMarshal());
        //todo
        //System.out.println("Your booking facility name: " + s5C.unMarshal());
        //System.out.println("Your booking time: " + s5C.unMarshal());
    }

    private void enterFacType() {
        System.out.println("1. Lecture Theater");
        System.out.println("2. Meeting Room");
        int type = readFacType("Select the type of facility that you want auto book: ");
        s5C.setType(type);
    }
}
