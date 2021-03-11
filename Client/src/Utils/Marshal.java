package Utils;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *
     * @param collectedMsg
     * @return the bytes contains length information, first send of UDP
     */
    public static byte[] marshalMsgHeader(ArrayList<Object> collectedMsg){
        // create a list<Byte>
        List<Byte> constructedMsg = new ArrayList<>();
        // loop the collectedMsg and marshall
        for(Object obj : collectedMsg){
            if(obj.getClass() == Integer.class){
                // all int value occupy 4 bytes
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(4))));
            }else if(obj.getClass() == String.class){
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(((String) obj).length()))));
            }
        }
        System.out.println(constructedMsg);
        // convert Byte[] to byte[] for UDP
        return ArrayUtils.toPrimitive(constructedMsg.toArray(new Byte[0]));
    }

    /**
     *
     * @param collectedMsg {@code ArrayList<Object>} collect data in each service
     * @return {@code byte[]} the bytes contains data information, send followed by marshalMsgHeader
     */
    public static byte[] marshalMsgData(ArrayList<Object> collectedMsg, Boolean isAck){
        // create a list<Byte>
        List<Byte> constructedMsg = new ArrayList<>();
        // loop the collectedMsg and marshall
        for(Object obj : collectedMsg){
            if(obj.getClass() == Integer.class){
                if(!isAck) {
                    // the length of int data
                    constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(4))));
                }
                // the actual int data
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt((Integer) obj))));
            }else if(obj.getClass() == String.class){
                // the length of String data
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalInt(((String) obj).length()))));
                // the actual string data
                constructedMsg.addAll(Arrays.asList(ArrayUtils.toObject(marshalString((String) obj))));
            }
        }
//        System.out.println(constructedMsg);
        // convert Byte[] to byte[] for UDP
        return ArrayUtils.toPrimitive(constructedMsg.toArray(new Byte[0]));
    }
}
