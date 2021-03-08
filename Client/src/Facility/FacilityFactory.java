package Facility;

import callback.CallBack;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacilityFactory extends Remote {
    FacilityIF query(String facility_name, int no_of_days) throws RemoteException;
    int BookFacility(String facility_name, String start_date, String end_date) throws RemoteException;
    Boolean ChangeBookedFacility(int Booking_ID, int time_offset) throws RemoteException;
    AutoBook autoBookingFacility(String facility_name) throws RemoteException;
    void register(CallBack cb) throws RemoteException;
    void deregister(CallBack cb) throws RemoteException;
}
