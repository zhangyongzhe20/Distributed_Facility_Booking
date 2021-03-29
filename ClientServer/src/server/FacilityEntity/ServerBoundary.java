package server.FacilityEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public interface ServerBoundary {
    public default void processRequest(byte[] dataTobeUnmarshal) throws TimeoutException, IOException{}
    public default void processRequest(byte[] dataTobeUnmarshal, ArrayList<BookingID> BookingIDArrayList) throws TimeoutException, IOException {}
    public default void reply() throws IOException, TimeoutException {}
}
