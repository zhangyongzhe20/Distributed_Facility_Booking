import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SerializeTest {
    @Test
    void marshallMsg() {
        // used array list to collect data
        ArrayList<Object> collectedMsg = new ArrayList<>();
        // add an int
        collectedMsg.add(1);
        // add a string
        collectedMsg.add("id");
        // marshall all data and ready for UDP
        byte[] udpData = Marshal.marshalMsg(collectedMsg);
        // unmarshal to get first data(int)
        int intValue = Marshal.unmarshalInteger(Marshal.marshalMsg(collectedMsg), 0);
        // unmarshal to get the second data(String)
        String stringValue = Marshal.unmarshalString(Marshal.marshalMsg(collectedMsg), 4, 6);
        // check
        assertEquals(1, intValue);
        assertEquals("id", stringValue);

    }
}