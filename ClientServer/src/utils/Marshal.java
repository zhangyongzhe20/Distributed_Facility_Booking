package utils;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Z. YZ
 */
public class Marshal {
    /**
     * marshal.Marshal int into bytes, using Big-Endian format
     * @param x {@code int}
     * @return {@code byte[]}
     */
    public static byte[] marshalInt(int x){
        return new byte[]{
                (byte)(x >> 24),
                (byte)(x >> 16),
                (byte)(x >> 8),
                (byte)(x)
        };
    }



    /**
     * marshal.Marshal String into bytes
     * @param s {@code String}
     * @return {@code byte[]}
//   * @throws UnsupportedEncodingExceptions
     */
    public static byte[] marshalString(String s) {
//        byte[] ret = new byte[s.length()];
//        for(int i = 0; i < s.length(); i++) {
//            ret[i] = (byte)s.charAt(i);
//        }
//        return ret;
        return s.getBytes();
    }

    /**
     * Used to marshal an arraylist obeject and convert to bytes for transmission
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @return {@code byte[]} the bytes contains data information, send followed by marshalMsgHeader
     */
    public static byte[] marshalMsgData(ArrayList<Object> collectedMsg, Boolean isAck){
        // create a list<Byte>
        List<Byte> constructedMsg = new ArrayList<>();
        // loop the collectedMsg and marshall header first
        for(int i = 0; i<3; i++){
            constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt((Integer) collectedMsg.get(i)))));
        }
        if(!isAck) {
            // loop the collectedMsg and marshall data section
            for (int i = 3; i < collectedMsg.size(); i++) {
                if (collectedMsg.get(i) == null) {
                    System.out.println("the collectedMsg is null!");
                } else {
                    if (collectedMsg.get(i).getClass() == Integer.class) {
                        // the length of int data
                        constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(4))));
                        // the actual int data
                        constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt((Integer) collectedMsg.get(i)))));
                    } else if (collectedMsg.get(i).getClass() == String.class) {
                        // the length of String data
                        constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(((String) collectedMsg.get(i)).length()))));
                        // the actual string data
                        constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalString((String) collectedMsg.get(i)))));
                    }
                }
            }
        }
        // convert Byte[] to byte[] for UDP
        return ArrayUtils.toPrimitive(constructedMsg.toArray(new Byte[0]));
    }
}
