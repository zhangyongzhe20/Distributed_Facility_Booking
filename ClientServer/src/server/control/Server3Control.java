package server.control;

import server.FacilityEntity.BookingID;
import server.FacilityEntity.Facility;
import utils.Marshal;
import utils.UnMarshal;
import utils.MonthDateParser;

import java.io.IOException;
import java.lang.instrument.UnmodifiableClassException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import static server.control.Control.msgIDresponseMap;


public class Server3Control extends ControlFactory implements ControlChangeFactory{
    private int bookingID;
    private int offset;

    private boolean bookingIDExist;
    private boolean offsetInBound;
    private boolean collisionStatus; // 1: fully has no vacancy; 2: partial vacancy can postpone; 3: partial vacancy can advance; 4: ok
    private String idAssociatedBookingInfo;
    private BookingID changedBookingID;

    private int day;
    private String facilityName;
    private int startIndex;
    private int endIndex;

    private MonthDateParser mdParser;

    public Server3Control() throws SocketException, UnknownHostException {
        super();
        this.bookingIDExist = false;
        this.collisionStatus = true;
        this.offsetInBound = false;
        this.mdParser = new MonthDateParser();
        this.idAssociatedBookingInfo="";
    }

    /**
     * Unmarshal Msg received from server
     * @param dataTobeUnmarshal
     * @param BookingIDArrayList
     * @return
     * @throws IOException
     */
    @Override
    public String unMarshal(byte[] dataTobeUnmarshal, ArrayList<BookingID> BookingIDArrayList) throws IOException {
        this.dataToBeUnMarshal = dataTobeUnmarshal;
        if (this.dataToBeUnMarshal.length != 0)
        {
            this.bookingID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 16);
            System.out.println("[Server3 Control]   --unMarshal-- Received BookingID: "+bookingID);

            this.offset = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal, 24);
            System.out.println("[Server3 Control]   --unMarshal-- Wanted change offset is: "+offset);

            // Check BookingID exist or not
            for (BookingID bid: BookingIDArrayList){
                if (bid.getID() == this.bookingID && !(bid.isCancel())){
                    System.out.println("[Server3 Control]   --unMarshal-- The bookingID exist");
                    this.bookingIDExist = true;
                    this.idAssociatedBookingInfo = bid.getBookingInfoString();
                    parseBookingInfo(idAssociatedBookingInfo);
                    break;
                }
            }// end for

            // If bookingID exist. Check if offset is in bound
            if (this.bookingIDExist){
                if (offset<0 && (startIndex+offset)<1){
                    this.offsetInBound = false;
                } else if (offset>0 && (endIndex+offset)>11){
                    this.offsetInBound = false;
                } else{
                    this.offsetInBound =true;
                }
            }// endif

            // If offset is in bound. Check booking time has collision or not.
            if (this.offsetInBound){
                changeBookingTime(facilityArrayList);
                if (!this.collisionStatus){
                    // if change is success. Set the previous bookingID status to cancel.
                    for (BookingID bid: BookingIDArrayList){
                        if (bid.getID() == this.bookingID){
                            bid.cancelBooking();
                            break;
                        }
                    }
                    // And create a new one
                    System.out.println("[Server3 Control]   --unMarshal-- Create a new bookingID");
                    changedBookingID = new BookingID(BookingIDArrayList.size()+1, this.day, this.facilityName,
                            this.startIndex+offset+7, this.endIndex+offset+7);
                    BookingIDArrayList.add(changedBookingID);
                }
            } // end if

        }
        return null;
    }

    /**
     * Send Marshaled Data corresponding to each 3 cases to Client
     * @throws TimeoutException
     * @throws IOException
     */
    @Override
    public void marshalAndSend() throws TimeoutException, IOException{
        int msgID = UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,4);
        if (UnMarshal.unmarshalInteger(this.dataToBeUnMarshal,0) == 0){
            // Msg Type is ACK
            System.out.println("[Server3]   --marshalAndSend--  Received ACK msg");
        } else if (!this.bookingIDExist){
            // Booking ID doesnot exist
            this.marshaledData = Marshal.marshalString("The bookingID does not exist.");
            this.status = new byte[]{0,0,0,0};
            //send(this.marshaledData);
            send(this.marshaledData, this.status, msgID);
        } else if (!this.offsetInBound){
            this.marshaledData = Marshal.marshalString("Shift slots out of time range 8am-6pm. Pls choose another slot");
            this.status = new byte[]{0,0,0,0};
            send(this.marshaledData, this.status, msgID);
        }

        else {
            // Booking ID exist
            System.out.println("[Server3]   --marshalAndSend--  Msg Type is request");
            if (this.collisionStatus){
                System.out.println("[Server3]   --marshalAndSend--  The intended changed slot is fully not available.");  // TODO: Delete this print after client can parse
                this.marshaledData = Marshal.marshalString("The intended changed slot is not available.");
                this.status = new byte[]{0,0,0,0};
                send(this.marshaledData, this.status, msgID);
            } else {
                System.out.println("[Server3]   --marshalAndSend--  Change is success. "+this.changedBookingID.getBookingInfoString());
                this.marshaledData = Marshal.marshalString(this.changedBookingID.getBookingInfoString());
                this.status = new byte[]{0,0,0,1};
               // send(this.marshaledData);
                //TODO
                send(this.marshaledData, Marshal.marshalString(getLatestQueryInfo(7, this.facilityName)), this.facilityName, msgID);
            }
        }
        this.collisionStatus = true;
        this.bookingIDExist = false;
        this.offsetInBound = false;
        this.dataToBeUnMarshal = new byte[0];
    }

    /**
     * Send Data
     * @param sendData
     * @throws IOException
     */
    @Override
    public void send(byte[] sendData) throws IOException{
        System.out.println("[Server2]   --send--    Collision Status: "+this.collisionStatus + "ID exist: "+this.bookingIDExist);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, this.status, sendData);
        udpSever.UDPsend(addAck_msg);
    }

    /**
     * Send Data
     * @param sendData
     * @param status
     * @param msgID
     * @throws IOException
     */
    public void send(byte[] sendData, byte[] status, int msgID) throws IOException{
        System.out.println("[Server2]   --send--    Collision Status: "+this.collisionStatus + "ID exist: "+this.bookingIDExist);
        this.ackType = new byte[]{0,0,0,1};
        byte[] addAck_msg = concat(ackType, status, sendData);
        udpSever.UDPsend(addAck_msg);
        //update table
        msgIDresponseMap.put(msgID, addAck_msg);
    }

    /**
     * Change booking time of the required facility
     * @param facilityArrayList
     */
    public void changeBookingTime(ArrayList<Facility> facilityArrayList){
        for (Facility fc: facilityArrayList) {
            if (fc.getFacilityName().equals(facilityName)){
                // only one booking slot
                if ((endIndex-startIndex)==1){
                    if (!fc.checkAvailability(this.day, this.startIndex+offset))
                    {// has collision
                        System.out.println("check slot: "+fc.checkAvailability(day, this.startIndex+offset));
                    }else{// no collision
                        fc.cancelBooking(this.day, this.startIndex);
                        fc.bookAvailability(this.day, this.startIndex+offset);
                        this.collisionStatus =false;
                    }
                }else {// two booking slots
                    System.out.println("offset "+offset);
                    if (this.offset == 1){
                        // postpone one slot
                        if (fc.checkAvailability(this.day, this.endIndex)){
                            this.collisionStatus = false;
                            fc.cancelBooking(this.day, this.startIndex);
                            fc.bookAvailability(this.day, this.startIndex+2);
                        }
                    } else if (this.offset == -1){
                        // advance one slot
                        if (fc.checkAvailability(this.day, this.startIndex-1)){
                            this.collisionStatus = false;
                            fc.cancelBooking(this.day, this.endIndex-1);
                            fc.bookAvailability(this.day, this.startIndex-1);
                        }
                    } else {
                        // advance or postpone more than one slot
                            if (fc.checkAvailability(this.day, this.startIndex+offset) && fc.checkAvailability(this.day, this.endIndex+offset-1)){
                                // both 2 slots have availability
                                this.collisionStatus = false;
                                fc.cancelBooking(this.day, this.startIndex);
                                fc.cancelBooking(this.day, this.endIndex-1);
                                fc.bookAvailability(this.day, this.startIndex+offset);
                                fc.bookAvailability(this.day, this.endIndex+offset-1);
                            }
                    }
                    break;
                }
                break;
            }
        }
    }

    /**
     * Parse the received booking info
     * @param bookingInfo
     */
    @Override
    public void parseBookingInfo(String bookingInfo){
        this.day = mdParser.StringDayToInt(bookingInfo.substring(9,11)) - mdParser.getDate();
        this.facilityName = bookingInfo.substring(12,15);
        this.startIndex = Integer.parseInt(bookingInfo.substring(16,18))-7;
        this.endIndex = Integer.parseInt(bookingInfo.substring(18,20))-7;

        System.out.println("[Server3 Control]   --parseBooking Info day: "+day+" facilityName: "+ facilityName
                +"  Start Index: "+startIndex+"  End index: "+endIndex);
    }
}
