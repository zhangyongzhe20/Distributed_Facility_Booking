import javax.security.auth.callback.Callback;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FacilityFactoryImpl extends UnicastRemoteObject implements FacilityFactory {


    protected FacilityFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public FacilityIF query(String facility_name, int no_of_days) throws RemoteException {
        return null;
    }

    @Override
    public int BookFacility(String facility_name, String start_date, String end_date) throws RemoteException {
        return 0;
    }

    @Override
    public Boolean ChangeBookedFacility(int Booking_ID, int time_offset) {
        return null;
    }


    @Override
    public AutoBook autoBookingFacility(String facility_name) throws RemoteException {
        return null;
    }

    @Override
    public void register(Callback cb) throws RemoteException {

    }

    @Override
    public void deregister(Callback cb) throws RemoteException {

    }
}
