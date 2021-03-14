import utils.Marshal;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class SerializeTest {
    @Test
    void marshallMsg() {
        // used array list to collect data
        ArrayList<Object> collectedMsg = new ArrayList<>();
         //add an int
        collectedMsg.add(1);
         //add a string
        collectedMsg.add("Meeting Room 01");

        // marshall all data and ready for UDP
        byte[] udpMsgHeader = Marshal.marshalMsgHeader(collectedMsg);
        byte[] udpMsgData = Marshal.marshalMsgData(collectedMsg, false);

        //todo, update test
         //simulate the bytes are received at server
//        ArrayList<Object> ReceivedMsg = UnMarshal.unMarshalMsg(udpMsgHeader, udpMsgData);

        // check
//        assertEquals(1, ReceivedMsg.get(0));
//        assertEquals("Meeting Room 01", ReceivedMsg.get(1));

    }
}