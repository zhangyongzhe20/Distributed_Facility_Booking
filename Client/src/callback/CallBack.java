package callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallBack extends Remote
{
    String monitorFacility() throws RemoteException;
}
