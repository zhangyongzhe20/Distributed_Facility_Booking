package Utils;

import java.util.ArrayList;

public class UnMarshal {
//    Map<Integer, String> facilityTable = new HashMap<>()
//    {{
//        put(1, "Meeting Room 01");
//        put(2, "Meeting Room 02");
//        put(3, "Lecture Theater 01");
//        put(4, "Lecture Theater 02");
//    }};
//

    /**
     * Unmarshal bytes into int, using Big-Endian format
     * @param b {@code byte[]}
     * @param start {@code int} starting index
     * @return {@code int}
     */
    public static int unmarshalInteger(byte[] b, int start){
        return b[start] << 24|(b[start+1] & 0xFF) << 16|(b[start+2] & 0xFF) << 8|(b[start+3] & 0xFF);
    }


    /**
     * Unmarshal bytes into String
     * @param b {@code byte[]}
     * @param start {@code int} starting index
     * @param end {@code int} ending index
     * @return {@code String}
     */
    public static String unmarshalString(byte[] b, int start, int end){
        char[] c = new char[end - start];
        for(int i = start; i < end; i++) {
            c[i-start] = (char)(b[i]);
        }
        return new String(c);
    }

    /**
     *
     * @param udpMsgHeader {@code byte[]} the received header contains length information
     * @param udpMsgData {@code byte[]} the received msg contains the actual data
     * @return the unmarshal data stores in array list
     */
    public static ArrayList<Object> unMarshalMsg(byte[] udpMsgHeader, byte[] udpMsgData){
         ArrayList<Object> collectedMsg = new ArrayList<>();
         // TODO: assume the first data is int, and sec is a string;
         // Todo: In real each services, the pre-knowledge of unmarshal is needed; Table?

        int firVariableLength = unmarshalInteger(udpMsgHeader, 0);
        int secVariableLength = unmarshalInteger(udpMsgHeader, 4);
//        System.out.println("length: " + firVariableLength + secVariableLength);

        int firViarable = unmarshalInteger(udpMsgData, 0);
        String secViarable = unmarshalString(udpMsgData, firVariableLength, firVariableLength + secVariableLength);
        collectedMsg.add(firViarable);
        collectedMsg.add(secViarable);
        return collectedMsg;
    }
}
