import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FacilityImplment extends UnicastRemoteObject implements FacilityIF {
    private String facilityName;
    private String availableSlots;

    protected FacilityImplment(String name, String slots) throws RemoteException {
        super();
        facilityName = name;
        availableSlots = slots;
    }


    @Override
    public String getFacilityName() throws RemoteException {
        return facilityName;
    }

    @Override
    public String getAvailableSlots() throws RemoteException {
        return availableSlots;
    }
}
