package Facility;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import callback.CallBack;

public class FacilityFactoryImpl extends UnicastRemoteObject implements FacilityFactory {


    public FacilityFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public FacilityIF query(String facility_name, int no_of_days) throws RemoteException {
        FacilityIF queryFacility;
        System.out.println("Query:...");
        return null;
    }

    @Override
    public int BookFacility(String facility_name, String start_date, String end_date) throws RemoteException {
        return 0;
    }

    @Override
    public Boolean ChangeBookedFacility(int Booking_ID, int time_offset) throws RemoteException {
        return null;
    }


    @Override
    public AutoBook autoBookingFacility(String facility_name) throws RemoteException {
        return null;
    }

    @Override
    public void register(CallBack cb) throws RemoteException {

    }

    @Override
    public void deregister(CallBack cb) throws RemoteException {

    }
}
