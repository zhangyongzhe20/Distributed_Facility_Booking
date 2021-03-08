package callback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {

    public CallBackImpl() throws RemoteException {
        super();
    }

    @Override
    public String monitorFacility() throws RemoteException {
        return null;
    }
}
