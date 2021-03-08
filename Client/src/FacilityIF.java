import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacilityIF extends Remote {
    String getFacilityName() throws RemoteException;
    String getAvailableSlots() throws RemoteException;
}
