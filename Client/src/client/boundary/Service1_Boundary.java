package client.boundary;
import client.control.Service1Control;
public class Service1_Boundary extends Boundary {
    private Service1Control s1C = new Service1Control();
    @Override
    public void displayMain() {
        enterFacilityName();
        enterNumOfDays();
        // marshal
         s1C.marshal();
        // send and receive
        s1C.sendAndReceive();
        //display reply
        displayReply();
    }

    @Override
    public void displayReply() {
        //todo
        System.out.println("Available intervals: ");
    }

    private void enterFacilityName() {
        String name = readInputString("Enter Facility Name: ");
        s1C.setFacName(name);
    }

    private void enterNumOfDays(){
        int days = readInputInteger("Enter the number of days: ");
        s1C.setNumOfDays(days);
    }
}
